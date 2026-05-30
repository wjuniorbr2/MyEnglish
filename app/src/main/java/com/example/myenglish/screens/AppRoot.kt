package com.example.myenglish.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myenglish.R
import com.example.myenglish.components.ArtButton
import com.example.myenglish.components.BugReportOverlay
import com.example.myenglish.components.StudentNameDialog
import com.example.myenglish.data.HomeworkData
import com.example.myenglish.data.PracticeData
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
    var returnToHomeworkScreen by remember { mutableStateOf<String?>(null) }
    var returnToPracticeScreen by remember { mutableStateOf<String?>(null) }
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
        resetAttemptLists(sentenceCount, answers, plays, hints, firstCorrect)
    }

    fun saveCurrentAttempt() {
        saveAttempt(prefs, activeHomeworkLesson, submitStep, score, message, answers, plays, hints, firstCorrect)
    }

    fun clearCurrentAttempt(lessonName: String) {
        clearAttempt(prefs, lessonName)
    }

    fun restoreCurrentAttempt(lessonName: String, sentenceCount: Int): Boolean {
        val restoredAttempt = restoreAttempt(prefs, lessonName, sentenceCount, answers, plays, hints, firstCorrect) ?: return false
        submitStep = restoredAttempt.submitStep
        score = restoredAttempt.score
        message = restoredAttempt.message
        return true
    }

    LaunchedEffect(Unit) {
        resetAttempt(HomeworkData.sentencesForLesson("Lesson 1").size)
    }

    fun listeningDoneKey(lessonName: String) = lessonName.lowercase().replace(" ", "_") + "_listening_done"
    fun writtenDoneKey(lessonName: String) = lessonName.lowercase().replace(" ", "_") + "_written_done"
    fun spokenDoneKey(lessonName: String) = lessonName.lowercase().replace(" ", "_") + "_spoken_done"

    fun listeningDoneForLesson(lessonName: String): Boolean {
        doneVersion
        return prefs.getBoolean(listeningDoneKey(lessonName), false)
    }

    fun writtenDoneForLesson(lessonName: String): Boolean {
        doneVersion
        return prefs.getBoolean(writtenDoneKey(lessonName), false)
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

    fun markWrittenDone(lessonName: String) {
        prefs.edit().putBoolean(writtenDoneKey(lessonName), true).apply()
        doneVersion++
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
        returnToHomeworkScreen = null
        returnToPracticeScreen = null
        screen = "homework"
    }

    fun openWrittenHomework() {
        activeHomeworkLesson = selectedLesson
        returnToHomeworkScreen = null
        returnToPracticeScreen = null
        screen = "writtenHomework"
    }

    fun openSpokenHomework() {
        activeHomeworkLesson = selectedLesson
        returnToHomeworkScreen = null
        returnToPracticeScreen = null
        screen = "spokenHomework"
    }

    fun openPractice() {
        activeHomeworkLesson = selectedLesson
        returnToHomeworkScreen = null
        returnToPracticeScreen = null
        screen = "practiceMenu"
    }

    fun openWrittenPractice() {
        activeHomeworkLesson = selectedLesson
        screen = "writtenPractice"
    }

    fun openListeningPractice() {
        activeHomeworkLesson = selectedLesson
        screen = "listeningPractice"
    }

    fun openSpokenPractice() {
        activeHomeworkLesson = selectedLesson
        screen = "practice"
    }

    fun openBookFromHomework(currentHomeworkScreen: String) {
        selectedLesson = activeHomeworkLesson
        returnToHomeworkScreen = currentHomeworkScreen
        returnToPracticeScreen = null
        screen = "book"
    }

    fun openBookFromPractice(currentPracticeScreen: String) {
        selectedLesson = activeHomeworkLesson
        returnToPracticeScreen = currentPracticeScreen
        returnToHomeworkScreen = null
        screen = "book"
    }

    fun goBackToHomework() {
        val homeworkScreen = returnToHomeworkScreen
        returnToHomeworkScreen = null
        if (homeworkScreen != null) screen = homeworkScreen
    }

    fun goBackToPractice() {
        val practiceScreen = returnToPracticeScreen
        returnToPracticeScreen = null
        if (practiceScreen != null) screen = practiceScreen
    }

    fun openBookFromLesson() {
        returnToHomeworkScreen = null
        returnToPracticeScreen = null
        screen = "book"
    }

    fun leaveBookToLesson() {
        returnToHomeworkScreen = null
        returnToPracticeScreen = null
        screen = "lesson"
    }

    BackHandler(enabled = screen != "home") {
        if (screen == "homework") {
            screen = "lesson"
        } else if (screen == "writtenHomework") {
            screen = "lesson"
        } else if (screen == "spokenHomework") {
            screen = "lesson"
        } else if (screen == "practiceMenu") {
            screen = "lesson"
        } else if (screen == "writtenPractice") {
            screen = "practiceMenu"
        } else if (screen == "listeningPractice") {
            screen = "practiceMenu"
        } else if (screen == "practice") {
            screen = "practiceMenu"
        } else if (screen == "book") {
            leaveBookToLesson()
        } else if (screen == "lesson") {
            screen = "home"
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (showNameDialog) {
            StudentNameDialog(studentName) { newName ->
                studentName = newName
                prefs.edit().putString("student_name", newName).apply()
                showNameDialog = false
            }
        }

        when (screen) {
            "home" -> Home(
                studentName = displayStudentName(studentName),
                onChangeName = { showNameDialog = true },
                openLesson = { lesson ->
                    selectedLesson = lesson
                    showChoices = false
                    returnToHomeworkScreen = null
                    returnToPracticeScreen = null
                    screen = "lesson"
                }
            )

            "lesson" -> Lesson(
                name = selectedLesson,
                listeningDone = listeningDoneForLesson(selectedLesson),
                writtenDone = writtenDoneForLesson(selectedLesson),
                spokenDone = spokenDoneForLesson(selectedLesson),
                showChoices = showChoices,
                showHomework = { showChoices = true },
                openBook = { openBookFromLesson() },
                openPractice = { openPractice() },
                openListening = { openListeningHomework() },
                openWritten = { openWrittenHomework() },
                openSpoken = { openSpokenHomework() },
                back = { screen = "home" }
            )

            "book" -> BookScreen(
                lessonName = selectedLesson,
                back = { leaveBookToLesson() }
            )

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
                    setScore = { score = it; saveCurrentAttempt() },
                    setStep = { submitStep = it; saveCurrentAttempt() },
                    setMsg = { message = it; saveCurrentAttempt() },
                    setReport = { report = it },
                    onAttemptChanged = { saveCurrentAttempt() },
                    done = { markListeningDone(activeHomeworkLesson) },
                    back = { screen = "lesson" }
                )
            }

            "writtenHomework" -> WrittenHomework(
                name = activeHomeworkLesson,
                studentName = displayStudentName(studentName),
                sentences = HomeworkData.writtenSentencesForLesson(activeHomeworkLesson),
                done = { markWrittenDone(activeHomeworkLesson) },
                back = { screen = "lesson" }
            )

            "spokenHomework" -> SpokenHomework(
                name = activeHomeworkLesson,
                studentName = displayStudentName(studentName),
                sentences = HomeworkData.spokenSentencesForLesson(activeHomeworkLesson),
                done = { markSpokenDone(activeHomeworkLesson) },
                back = { screen = "lesson" }
            )

            "practice" -> PracticeScreen(
                name = activeHomeworkLesson,
                studentName = displayStudentName(studentName),
                sentences = PracticeData.sentencesForLesson(activeHomeworkLesson),
                back = { screen = "practiceMenu" }
            )

            "practiceMenu" -> PracticeMenu(
                name = activeHomeworkLesson,
                openWritten = { openWrittenPractice() },
                openListening = { openListeningPractice() },
                openSpoken = { openSpokenPractice() },
                back = { screen = "lesson" }
            )

            "writtenPractice" -> WrittenPracticeScreen(
                name = activeHomeworkLesson,
                studentName = displayStudentName(studentName),
                sentences = PracticeData.writtenSentencesForLesson(activeHomeworkLesson),
                back = { screen = "practiceMenu" }
            )

            "listeningPractice" -> ListeningPracticeScreen(
                name = activeHomeworkLesson,
                studentName = displayStudentName(studentName),
                sentences = PracticeData.listeningSentencesForLesson(activeHomeworkLesson),
                back = { screen = "practiceMenu" }
            )
        }

        if (screen == "homework" || screen == "writtenHomework" || screen == "spokenHomework") {
            ArtButton(
                text = "Go to lesson",
                onClick = { openBookFromHomework(screen) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.68f)
                    .padding(bottom = 18.dp),
                backgroundResId = R.drawable.redbutton,
                heightDp = 54,
                fontSize = 17
            )
        }

        if (screen == "practice" || screen == "writtenPractice" || screen == "listeningPractice") {
            ArtButton(
                text = "Go to lesson",
                onClick = { openBookFromPractice(screen) },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.68f)
                    .padding(bottom = 18.dp),
                backgroundResId = R.drawable.redbutton,
                heightDp = 54,
                fontSize = 17
            )
        }

        if (screen == "book" && returnToHomeworkScreen != null) {
            ArtButton(
                text = "Go back to homework",
                onClick = { goBackToHomework() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.78f)
                    .padding(bottom = 18.dp),
                backgroundResId = R.drawable.redbutton,
                heightDp = 54,
                fontSize = 16
            )
        }

        if (screen == "book" && returnToPracticeScreen != null) {
            ArtButton(
                text = "Go back to practice",
                onClick = { goBackToPractice() },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.78f)
                    .padding(bottom = 18.dp),
                backgroundResId = R.drawable.redbutton,
                heightDp = 54,
                fontSize = 16
            )
        }

        BugReportOverlay(
            studentName = displayStudentName(studentName),
            lessonName = if (screen == "home") "" else activeHomeworkLesson,
            currentScreen = screen
        )
    }
}
