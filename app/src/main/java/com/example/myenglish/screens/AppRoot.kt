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
import com.example.myenglish.utils.clearAttempt
import com.example.myenglish.utils.displayStudentName
import com.example.myenglish.utils.resetAttemptLists
import com.example.myenglish.utils.restoreAttempt
import com.example.myenglish.utils.saveAttempt

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
    var doneVersion by remember { mutableIntStateOf(0) }

    val answers = remember { mutableStateListOf<String>() }
    val plays = remember { mutableStateListOf<Int>() }
    val hints = remember { mutableStateListOf<Int>() }
    val firstCorrect = remember { mutableStateListOf<Boolean>() }

    var submitStep by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var message by remember { mutableStateOf("") }
    var report by remember { mutableStateOf("") }

    fun resetAttempt(sentenceCount: Int) {
        resetAttemptLists(
            sentenceCount = sentenceCount,
            answers = answers,
            plays = plays,
            hints = hints,
            firstCorrect = firstCorrect
        )
    }

    fun saveCurrentAttempt() {
        saveAttempt(
            prefs = prefs,
            lessonName = activeHomeworkLesson,
            submitStep = submitStep,
            score = score,
            message = message,
            answers = answers,
            plays = plays,
            hints = hints,
            firstCorrect = firstCorrect
        )
    }

    fun clearCurrentAttempt(lessonName: String) {
        clearAttempt(
            prefs = prefs,
            lessonName = lessonName
        )
    }

    fun restoreCurrentAttempt(lessonName: String, sentenceCount: Int): Boolean {
        val restoredAttempt = restoreAttempt(
            prefs = prefs,
            lessonName = lessonName,
            sentenceCount = sentenceCount,
            answers = answers,
            plays = plays,
            hints = hints,
            firstCorrect = firstCorrect
        ) ?: return false

        submitStep = restoredAttempt.submitStep
        score = restoredAttempt.score
        message = restoredAttempt.message

        return true
    }

    LaunchedEffect(Unit) {
        resetAttempt(HomeworkData.sentencesForLesson("Lesson 1").size)
    }

    fun listeningDoneKey(lessonName: String): String {
        return lessonName.lowercase().replace(" ", "_") + "_listening_done"
    }

    fun spokenDoneKey(lessonName: String): String {
        return lessonName.lowercase().replace(" ", "_") + "_spoken_done"
    }

    fun listeningDoneForLesson(lessonName: String): Boolean {
        doneVersion
        return prefs.getBoolean(listeningDoneKey(lessonName), false)
    }

    fun spokenDoneForLesson(lessonName: String): Boolean {
        doneVersion
        return prefs.getBoolean(spokenDoneKey(lessonName), false)
    }

    fun markListeningDone(lessonName: String) {
        prefs.edit().putBoolean(listeningDoneKey(lessonName), true).apply()
        doneVersion++
        clearCurrentAttempt(lessonName)
    }

    fun markSpokenDone(lessonName: String) {
        prefs.edit().putBoolean(spokenDoneKey(lessonName), true).apply()
        doneVersion++
    }

    fun openListeningHomework() {
        val sentences = HomeworkData.sentencesForLesson(selectedLesson)
        activeHomeworkLesson = selectedLesson

        val restored = restoreCurrentAttempt(selectedLesson, sentences.size)

        if (!restored || submitStep == 2 || answers.size != sentences.size) {
            resetAttempt(sentences.size)
            submitStep = 0
            score = 0
            message = ""
            report = ""
        }

        screen = "homework"
    }

    fun openSpokenHomework() {
        activeHomeworkLesson = selectedLesson
        screen = "spokenHomework"
    }

    BackHandler(enabled = screen != "home") {
        if (screen == "homework") {
            screen = "lesson"
        } else if (screen == "spokenHomework") {
            screen = "lesson"
        } else if (screen == "book") {
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
                spokenDone = spokenDoneForLesson(selectedLesson),
                showChoices = showChoices,
                showHomework = { showChoices = true },
                openBook = { screen = "book" },
                openListening = { openListeningHomework() },
                openSpoken = { openSpokenHomework() },
                back = { screen = "home" }
            )
        }

        "book" -> {
            BookScreen(
                lessonName = selectedLesson,
                back = { screen = "lesson" }
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
                    saveCurrentAttempt()
                },
                setStep = {
                    submitStep = it
                    saveCurrentAttempt()
                },
                setMsg = {
                    message = it
                    saveCurrentAttempt()
                },
                setReport = {
                    report = it
                },
                onAttemptChanged = {
                    saveCurrentAttempt()
                },
                done = {
                    markListeningDone(activeHomeworkLesson)
                },
                back = {
                    screen = "lesson"
                }
            )
        }

        "spokenHomework" -> {
            val spokenSentences = HomeworkData.spokenSentencesForLesson(activeHomeworkLesson)

            SpokenHomework(
                name = activeHomeworkLesson,
                studentName = displayStudentName(studentName),
                sentences = spokenSentences,
                done = {
                    markSpokenDone(activeHomeworkLesson)
                },
                back = {
                    screen = "lesson"
                }
            )
        }
    }
}
