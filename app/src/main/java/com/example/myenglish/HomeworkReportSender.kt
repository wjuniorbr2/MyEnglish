package com.example.myenglish

import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

private const val HOMEWORK_REPORT_BASE_URL = "https://script.google.com/macros/s/"
private const val HOMEWORK_REPORT_DEPLOYMENT_ID = "AKfycbx8GP8uutwUJuK4Bc91l06O_OoDQVxTpc83iw31oKiGK3gEUszv8NL_MW8TyJZegGwf"
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
            success = responseCode in 200..299
            connection.disconnect()
        } catch (_: Exception) {
            success = false
        }

        onFinished(success)
    }.start()
}

private fun encodeForPost(value: String): String {
    return URLEncoder.encode(value, "UTF-8")
}
