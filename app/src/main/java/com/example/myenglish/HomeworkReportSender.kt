package com.example.myenglish

import android.util.Log
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private const val HOMEWORK_REPORT_URL = "https://script.google.com/macros/s/AKfycbyi6cDyYEuDI3n6XNZhDrbt-3I7h3cYg7cfaEkJCpKI4_TYVd-vIOHmWcfkDGrl4O3g/exec"
private const val REPORT_CONNECT_TIMEOUT_MS = 15000
private const val REPORT_READ_TIMEOUT_MS = 60000
private const val MAX_REPORT_REDIRECTS = 8
private const val REPORT_LOG_TAG = "MyEnglishReport"

@Volatile
private var latestReportFailure = ""

fun latestReportFailureDetail(): String = latestReportFailure

fun sendHomeworkReportToTeacher(
    studentName: String,
    lessonName: String,
    homeworkType: String,
    scoreText: String,
    report: String,
    onFinished: (Boolean) -> Unit
) {
    Thread {
        latestReportFailure = ""

        val success = try {
            val postData =
                "studentName=" + encodeForPost(studentName) +
                    "&lessonName=" + encodeForPost(lessonName) +
                    "&homeworkType=" + encodeForPost(homeworkType) +
                    "&scoreText=" + encodeForPost(scoreText) +
                    "&report=" + encodeForPost(report)

            postToReportEndpoint(postData)
        } catch (error: Exception) {
            recordReportFailure(
                "${error.javaClass.simpleName}: ${error.message ?: "No error message"}",
                error
            )
            false
        }

        onFinished(success)
    }.start()
}

fun sendBugReportToTeacher(
    studentName: String,
    lessonName: String,
    currentScreen: String,
    bugText: String,
    onFinished: (Boolean) -> Unit
) {
    Thread {
        latestReportFailure = ""

        val success = try {
            val postData =
                "studentName=" + encodeForPost(studentName) +
                    "&lessonName=" + encodeForPost(lessonName) +
                    "&homeworkType=" + encodeForPost("Bug report") +
                    "&reportType=" + encodeForPost("Bug report") +
                    "&currentScreen=" + encodeForPost(currentScreen) +
                    "&bugText=" + encodeForPost(bugText) +
                    "&scoreText=" + encodeForPost("0 / 0") +
                    "&report=" + encodeForPost(bugText)

            postToReportEndpoint(postData)
        } catch (error: Exception) {
            recordReportFailure(
                "${error.javaClass.simpleName}: ${error.message ?: "No error message"}",
                error
            )
            false
        }

        onFinished(success)
    }.start()
}

private fun postToReportEndpoint(postData: String): Boolean {
    val connection = (URL(HOMEWORK_REPORT_URL).openConnection() as HttpURLConnection).apply {
        requestMethod = "POST"
        connectTimeout = REPORT_CONNECT_TIMEOUT_MS
        readTimeout = REPORT_READ_TIMEOUT_MS
        doOutput = true
        instanceFollowRedirects = false
        useCaches = false
        setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
        setRequestProperty("Accept", "application/json, text/plain, */*")
        setRequestProperty("User-Agent", "MyEnglish-Android")
        setRequestProperty("Connection", "close")
    }

    OutputStreamWriter(connection.outputStream, Charsets.UTF_8).use { writer ->
        writer.write(postData)
        writer.flush()
    }

    val responseCode = connection.responseCode
    val redirectLocation = connection.getHeaderField("Location")

    if (responseCode in 300..399 && !redirectLocation.isNullOrBlank()) {
        Log.d(REPORT_LOG_TAG, "POST response $responseCode; following redirect")
        connection.disconnect()
        return readRedirectedReportResponse(
            initialUrl = URL(HOMEWORK_REPORT_URL),
            redirectLocation = redirectLocation
        )
    }

    val responseBody = readResponseBody(connection, responseCode)
    connection.disconnect()
    return evaluateReportResponse(responseCode, responseBody, "POST")
}

private fun readRedirectedReportResponse(
    initialUrl: URL,
    redirectLocation: String
): Boolean {
    var currentUrl = URL(initialUrl, redirectLocation)

    repeat(MAX_REPORT_REDIRECTS) { redirectIndex ->
        val connection = (currentUrl.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = REPORT_CONNECT_TIMEOUT_MS
            readTimeout = REPORT_READ_TIMEOUT_MS
            instanceFollowRedirects = false
            useCaches = false
            setRequestProperty("Accept", "application/json, text/plain, */*")
            setRequestProperty("User-Agent", "MyEnglish-Android")
            setRequestProperty("Connection", "close")
        }

        val responseCode = connection.responseCode
        val nextLocation = connection.getHeaderField("Location")

        if (responseCode in 300..399 && !nextLocation.isNullOrBlank()) {
            Log.d(REPORT_LOG_TAG, "Redirect ${redirectIndex + 1}: HTTP $responseCode")
            currentUrl = URL(currentUrl, nextLocation)
            connection.disconnect()
        } else {
            val responseBody = readResponseBody(connection, responseCode)
            connection.disconnect()
            return evaluateReportResponse(
                responseCode = responseCode,
                responseBody = responseBody,
                stage = "redirect ${redirectIndex + 1}"
            )
        }
    }

    recordReportFailure("Too many Apps Script redirects")
    return false
}

private fun evaluateReportResponse(
    responseCode: Int,
    responseBody: String,
    stage: String
): Boolean {
    val compactBody = responseBody
        .replace(Regex("\\s+"), " ")
        .trim()
        .take(1000)

    Log.d(REPORT_LOG_TAG, "$stage HTTP $responseCode: $compactBody")

    if (responseCode !in 200..299) {
        recordReportFailure("$stage returned HTTP $responseCode: $compactBody")
        return false
    }

    if (responseSaysSuccess(responseBody)) return true

    val serverMessage = Regex(
        "\\\"message\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"",
        RegexOption.IGNORE_CASE
    ).find(responseBody)?.groupValues?.getOrNull(1)

    recordReportFailure(
        serverMessage?.let { "Apps Script error: $it" }
            ?: "$stage returned an unexpected response: $compactBody"
    )
    return false
}

private fun readResponseBody(
    connection: HttpURLConnection,
    responseCode: Int
): String {
    val stream = if (responseCode in 200..399) {
        connection.inputStream
    } else {
        connection.errorStream
    }

    return stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() }.orEmpty()
}

private fun responseSaysSuccess(responseBody: String): Boolean {
    return Regex("\\\"status\\\"\\s*:\\s*\\\"success\\\"", RegexOption.IGNORE_CASE)
        .containsMatchIn(responseBody)
}

private fun recordReportFailure(message: String, error: Throwable? = null) {
    latestReportFailure = message
    if (error == null) {
        Log.e(REPORT_LOG_TAG, message)
    } else {
        Log.e(REPORT_LOG_TAG, message, error)
    }
}

private fun encodeForPost(value: String): String {
    return URLEncoder.encode(value, "UTF-8")
}
