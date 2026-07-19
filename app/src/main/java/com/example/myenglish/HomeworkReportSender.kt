package com.example.myenglish

import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private const val HOMEWORK_REPORT_BASE_URL = "https://script.google.com/macros/s/"
private const val HOMEWORK_REPORT_DEPLOYMENT_ID = "AKfycbyi6cDyYEuDI3n6XNZhDrbt-3I7h3cYg7cfaEkJCpKI4_TYVd-vlOHmWcfkDGrI4O3g"
private const val HOMEWORK_REPORT_URL = HOMEWORK_REPORT_BASE_URL + HOMEWORK_REPORT_DEPLOYMENT_ID + "/exec"
private const val REPORT_TIMEOUT_MS = 15000
private const val MAX_REPORT_REDIRECTS = 5

fun sendHomeworkReportToTeacher(
    studentName: String,
    lessonName: String,
    homeworkType: String,
    scoreText: String,
    report: String,
    onFinished: (Boolean) -> Unit
) {
    Thread {
        var success = false

        try {
            val postData =
                "studentName=" + encodeForPost(studentName) +
                    "&lessonName=" + encodeForPost(lessonName) +
                    "&homeworkType=" + encodeForPost(homeworkType) +
                    "&scoreText=" + encodeForPost(scoreText) +
                    "&report=" + encodeForPost(report)

            success = postToReportEndpoint(postData)
        } catch (_: Exception) {
            success = false
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
        var success = false

        try {
            val postData =
                "studentName=" + encodeForPost(studentName) +
                    "&lessonName=" + encodeForPost(lessonName) +
                    "&homeworkType=" + encodeForPost("Bug report") +
                    "&reportType=" + encodeForPost("Bug report") +
                    "&currentScreen=" + encodeForPost(currentScreen) +
                    "&bugText=" + encodeForPost(bugText) +
                    "&scoreText=" + encodeForPost("0 / 0") +
                    "&report=" + encodeForPost(bugText)

            success = postToReportEndpoint(postData)
        } catch (_: Exception) {
            success = false
        }

        onFinished(success)
    }.start()
}

private fun postToReportEndpoint(postData: String): Boolean {
    val connection = (URL(HOMEWORK_REPORT_URL).openConnection() as HttpURLConnection).apply {
        requestMethod = "POST"
        connectTimeout = REPORT_TIMEOUT_MS
        readTimeout = REPORT_TIMEOUT_MS
        doOutput = true
        instanceFollowRedirects = false
        setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
    }

    OutputStreamWriter(connection.outputStream, Charsets.UTF_8).use { writer ->
        writer.write(postData)
        writer.flush()
    }

    val responseCode = connection.responseCode
    val redirectLocation = connection.getHeaderField("Location")

    if (responseCode in 300..399 && !redirectLocation.isNullOrBlank()) {
        connection.disconnect()
        return readRedirectedReportResponse(
            initialUrl = URL(HOMEWORK_REPORT_URL),
            redirectLocation = redirectLocation
        )
    }

    val responseBody = readResponseBody(connection, responseCode)
    connection.disconnect()
    return responseCode in 200..299 && responseSaysSuccess(responseBody)
}

private fun readRedirectedReportResponse(
    initialUrl: URL,
    redirectLocation: String
): Boolean {
    var currentUrl = URL(initialUrl, redirectLocation)

    repeat(MAX_REPORT_REDIRECTS) {
        val connection = (currentUrl.openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = REPORT_TIMEOUT_MS
            readTimeout = REPORT_TIMEOUT_MS
            instanceFollowRedirects = false
        }

        val responseCode = connection.responseCode
        val nextLocation = connection.getHeaderField("Location")

        if (responseCode in 300..399 && !nextLocation.isNullOrBlank()) {
            currentUrl = URL(currentUrl, nextLocation)
            connection.disconnect()
        } else {
            val responseBody = readResponseBody(connection, responseCode)
            connection.disconnect()
            return responseCode in 200..299 && responseSaysSuccess(responseBody)
        }
    }

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
    return Regex("\"status\"\\s*:\\s*\"success\"", RegexOption.IGNORE_CASE)
        .containsMatchIn(responseBody)
}

private fun encodeForPost(value: String): String {
    return URLEncoder.encode(value, "UTF-8")
}
