package com.example.myenglish.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.StringTokenizer

fun currentDateTimeText(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return formatter.format(Date())
}

fun displayStudentName(fullName: String): String {
    val parts = StringTokenizer(fullName, " ")
    if (!parts.hasMoreTokens()) return "Student"

    val firstName = parts.nextToken()

    if (parts.hasMoreTokens()) {
        val secondName = parts.nextToken()
        if (secondName.isNotEmpty()) {
            return firstName + " " + Character.toUpperCase(secondName[0]) + "."
        }
    }

    return firstName
}

fun attemptPrefix(lessonName: String): String {
    return lessonName.lowercase(Locale.ROOT).replace(" ", "_") + "_listening_"
}

fun joinStrings(values: List<String>): String {
    val builder = StringBuilder()
    var i = 0
    while (i < values.size) {
        if (i > 0) builder.append("<|>")
        builder.append(values[i].replace("<|>", " "))
        i++
    }
    return builder.toString()
}

fun joinInts(values: List<Int>): String {
    val builder = StringBuilder()
    var i = 0
    while (i < values.size) {
        if (i > 0) builder.append(",")
        builder.append(values[i])
        i++
    }
    return builder.toString()
}

fun joinBooleans(values: List<Boolean>): String {
    val builder = StringBuilder()
    var i = 0
    while (i < values.size) {
        if (i > 0) builder.append(",")
        builder.append(if (values[i]) "1" else "0")
        i++
    }
    return builder.toString()
}