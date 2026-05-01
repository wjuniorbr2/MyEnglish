package com.example.myenglish.screens

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.myenglish.components.StudentNameDialog
import com.example.myenglish.data.HomeworkData
import com.example.myenglish.utils.attemptPrefix
import com.example.myenglish.utils.displayStudentName
import com.example.myenglish.utils.joinBooleans
import com.example.myenglish.utils.joinInts
import com.example.myenglish.utils.joinStrings

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", android.content.Context.MODE_PRIVATE) }

    var screen by remember { mutableStateOf("home") }
    var selectedLesson by remember { mutableStateOf("Lesson 1") }
    var activeHomeworkLesson by remember { mutableStateOf("Lesson 1") }
    var showChoices by remember { mutableStateOf(false) }
    var studentName by remember { mutableStateOf(prefs.getString("student_name", "") ?: "") }
    var showNameDialog by remember { mutableStateOf(studentName == "") }
    var lesson1Done by remember { mutableStateOf(prefs.getBoolean("lesson1_listening_done", false)) }
    var lesson2Done by remember { mutableStateOf(prefs.getBoolean("lesson2_listening_done", false)) }

    val answers = remember { mutableStateListOf<String>() }
    val plays = remember { mutableStateListOf<Int>() }
    val hints = remember { mutableStateListOf<Int>() }
    val firstCorrect = remember { mutableStateListOf<Boolean>() }

    var submitStep by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var report by remember { mutableStateOf("") }

    fun resetAttempt(sentenceCount: Int) {
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

    fun saveAttempt() {
        val prefix = attemptPrefix(activeHomeworkLesson)
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

    fun clearAttempt(lessonName: String) {
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

    fun restoreAttempt(lessonName: String, sentenceCount: Int): Boolean {
        val prefix = attemptPrefix(lessonName)
        if (!prefs.getBoolean(prefix + "in_progress", false)) return false

        resetAttempt(sentenceCount)

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

        submitStep = prefs.getInt(prefix + "submit_step", 0)
        score = prefs.getInt(prefix + "score", 0)
        message = prefs.getString(prefix + "message", "") ?: ""

        return true
    }

    LaunchedEffect(Unit) {
        resetAttempt(HomeworkData.sentencesForLesson("Lesson 1").size)
    }

    fun listeningDoneForLesson(lessonName: String): Boolean {
        return if (lessonName == "Lesson 1") {
            lesson1Done
        } else if (lessonName == "Lesson 2") {
            lesson2Done
        } else {
            false
        }
    }

    fun markListeningDone(lessonName: String) {
        if (lessonName == "Lesson 1") {
            lesson1Done = true
            prefs.edit().putBoolean("lesson1_listening_done", true).apply()
        } else if (lessonName == "Lesson 2") {
            lesson2Done = true
            prefs.edit().putBoolean("lesson2_listening_done", true).apply()
        }

        clearAttempt(lessonName)
    }

    fun openListeningHomework() {
        val sentences = HomeworkData.sentencesForLesson(selectedLesson)
        activeHomeworkLesson = selectedLesson

        val restored = restoreAttempt(selectedLesson, sentences.size)

        if (!restored || submitStep == 2 || answers.size != sentences.size) {
            resetAttempt(sentences.size)
            submitStep = 0
            score = 0
            message = ""
            report = ""
        }

        screen = "homework"
    }

    BackHandler(enabled = screen != "home") {
        if (screen == "homework") {
            screen = "lesson"
        } else if (screen == "lesson") {
            screen = "home"
        }
    }

    if (showNameDialog) {
        StudentNameDialog(studentName) { newName ->
            studentName = newName
            prefs.edit().putString("student_name", newName).apply()
            showNameDialog = false
        }
    }

    when (screen) {
        "home" -> {
            Home(
                studentName = displayStudentName(studentName),
                onChangeName = { showNameDialog = true },
                openLesson = { lesson ->
                    selectedLesson = lesson
                    showChoices = false
                    screen = "lesson"
                }
            )
        }

        "lesson" -> {
            Lesson(
                name = selectedLesson,
                listeningDone = listeningDoneForLesson(selectedLesson),
                showChoices = showChoices,
                showHomework = { showChoices = true },
                openListening = { openListeningHomework() },
                back = { screen = "home" }
            )
        }

        "homework" -> {
            val sentences = HomeworkData.sentencesForLesson(activeHomeworkLesson)
            val audioResId = HomeworkData.audioResIdForLesson(context, activeHomeworkLesson)

            Homework(
                name = activeHomeworkLesson,
                studentName = displayStudentName(studentName),
                sentences = sentences,
                audioResId = audioResId,
                answers = answers,
                plays = plays,
                hints = hints,
                firstCorrect = firstCorrect,
                submitStep = submitStep,
                score = score,
                msg = message,
                setScore = {
                    score = it
                    saveAttempt()
                },
                setStep = {
                    submitStep = it
                    saveAttempt()
                },
                setMsg = {
                    message = it
                    saveAttempt()
                },
                setReport = {
                    report = it
                },
                onAttemptChanged = {
                    saveAttempt()
                },
                done = {
                    markListeningDone(activeHomeworkLesson)
                },
                back = {
                    screen = "lesson"
                }
            )
        }
    }
}
