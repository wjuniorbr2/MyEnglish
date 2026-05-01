package com.example.myenglish.utils

import android.content.SharedPreferences

fun resetAttemptLists(
    sentenceCount: Int,
    answers: MutableList<String>,
    plays: MutableList<Int>,
    hints: MutableList<Int>,
    firstCorrect: MutableList<Boolean>
) {
    answers.clear()
    plays.clear()
    hints.clear()
    firstCorrect.clear()

    var i = 0
    while (i < sentenceCount) {
        answers.add("")
        plays.add(0)
        hints.add(0)
        firstCorrect.add(false)
        i++
    }
}

fun saveAttempt(
    prefs: SharedPreferences,
    lessonName: String,
    submitStep: Int,
    score: Int,
    message: String,
    answers: List<String>,
    plays: List<Int>,
    hints: List<Int>,
    firstCorrect: List<Boolean>
) {
    val prefix = attemptPrefix(lessonName)
    prefs.edit()
        .putBoolean(prefix + "in_progress", submitStep != 2)
        .putString(prefix + "answers", joinStrings(answers))
        .putString(prefix + "plays", joinInts(plays))
        .putString(prefix + "hints", joinInts(hints))
        .putString(prefix + "first_correct", joinBooleans(firstCorrect))
        .putInt(prefix + "submit_step", submitStep)
        .putInt(prefix + "score", score)
        .putString(prefix + "message", message)
        .apply()
}

fun clearAttempt(
    prefs: SharedPreferences,
    lessonName: String
) {
    val prefix = attemptPrefix(lessonName)
    prefs.edit()
        .remove(prefix + "in_progress")
        .remove(prefix + "answers")
        .remove(prefix + "plays")
        .remove(prefix + "hints")
        .remove(prefix + "first_correct")
        .remove(prefix + "submit_step")
        .remove(prefix + "score")
        .remove(prefix + "message")
        .apply()
}

data class RestoredAttempt(
    val submitStep: Int,
    val score: Int,
    val message: String
)

fun restoreAttempt(
    prefs: SharedPreferences,
    lessonName: String,
    sentenceCount: Int,
    answers: MutableList<String>,
    plays: MutableList<Int>,
    hints: MutableList<Int>,
    firstCorrect: MutableList<Boolean>
): RestoredAttempt? {
    val prefix = attemptPrefix(lessonName)
    if (!prefs.getBoolean(prefix + "in_progress", false)) return null

    resetAttemptLists(
        sentenceCount = sentenceCount,
        answers = answers,
        plays = plays,
        hints = hints,
        firstCorrect = firstCorrect
    )

    val savedAnswers = prefs.getString(prefix + "answers", "") ?: ""
    val answerParts = if (savedAnswers == "") emptyList() else savedAnswers.split("<|>")
    val playParts = (prefs.getString(prefix + "plays", "") ?: "").split(",")
    val hintParts = (prefs.getString(prefix + "hints", "") ?: "").split(",")
    val correctParts = (prefs.getString(prefix + "first_correct", "") ?: "").split(",")

    var i = 0
    while (i < sentenceCount) {
        if (i < answerParts.size) answers[i] = answerParts[i]
        if (i < playParts.size) plays[i] = playParts[i].toIntOrNull() ?: 0
        if (i < hintParts.size) hints[i] = hintParts[i].toIntOrNull() ?: 0
        if (i < correctParts.size) firstCorrect[i] = correctParts[i] == "1"
        i++
    }

    return RestoredAttempt(
        submitStep = prefs.getInt(prefix + "submit_step", 0),
        score = prefs.getInt(prefix + "score", 0),
        message = prefs.getString(prefix + "message", "") ?: ""
    )
}
