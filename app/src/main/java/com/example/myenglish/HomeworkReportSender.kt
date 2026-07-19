package com.example.myenglish

import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private const val HOMEWORK_REPORT_BASE_URL = "https://script.google.com/macros/s/"
private const val HOMEWORK_REPORT_DEPLOYMENT_ID = "AKfycbyi6cDyYEuDI3n6XNZhDrbt-3I7h3cYg7cfaEkJCpKI4_TYVd-vlOHmWcfkDGrI4O3g"
private const val HOMEWORK_REPORT_URL = HOMEWORK_REPORT_BASE_URL + HOMEWORK_REPORT_DEPLOYMENT_ID + "/exec"

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
    val url = URL(HOMEWORK_REPORT_URL)
    val connection = url.openConnection() as HttpURLConnection
    connection.requestMethod = "POST"
    connection.connectTimeout = 15000
    connection.readTimeout = 15000
    connection.doOutput = true
    connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")

    val writer = OutputStreamWriter(connection.outputStream, "UTF-8")
    writer.write(postData)
    writer.flush()
    writer.close()

    val responseCode = connection.responseCode
    connection.disconnect()

    return responseCode in 200..299
}

private fun encodeForPost(value: String): String {
    return URLEncoder.encode(value, "UTF-8")
}
