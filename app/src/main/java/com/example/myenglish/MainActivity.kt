package com.example.myenglish

import com.example.myenglish.screens.Home
import com.example.myenglish.screens.Lesson
import com.example.myenglish.components.StudentNameDialog
import com.example.myenglish.screens.AppWithSplash
import com.example.myenglish.components.Header
import com.example.myenglish.components.StrokeGlowTitle
import com.example.myenglish.components.StudentBadge
import com.example.myenglish.components.ArtButton
import com.example.myenglish.components.HomeworkIcon
import com.example.myenglish.data.HomeworkData
import com.example.myenglish.data.HomeworkSentence
import com.example.myenglish.utils.attemptPrefix
import com.example.myenglish.utils.currentDateTimeText
import com.example.myenglish.utils.displayStudentName
import com.example.myenglish.utils.joinBooleans
import com.example.myenglish.utils.joinInts
import com.example.myenglish.utils.joinStrings
import com.example.myenglish.utils.prepareSplashWindow
import com.example.myenglish.utils.restoreAppWindow
import com.example.myenglish.utils.canUseHint
import com.example.myenglish.utils.cleanAnswer
import com.example.myenglish.utils.isCorrectAnswer
import com.example.myenglish.utils.revealedHintText
import android.app.Activity
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.ui.theme.MyEnglishTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        prepareSplashWindow(window)
        overridePendingTransition(0, 0)

        setContent {
            MyEnglishTheme {
                AppWithSplash(this)
            }
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(0, 0)
    }
}

@Composable
fun AppRoot() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", Context.MODE_PRIVATE) }

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

@Composable
fun Homework(
    name: String,
    studentName: String,
    sentences: Array<HomeworkSentence>,
    audioResId: Int,
    answers: MutableList<String>,
    plays: MutableList<Int>,
    hints: MutableList<Int>,
    firstCorrect: MutableList<Boolean>,
    submitStep: Int,
    score: Int,
    msg: String,
    setScore: (Int) -> Unit,
    setStep: (Int) -> Unit,
    setMsg: (String) -> Unit,
    setReport: (String) -> Unit,
    onAttemptChanged: () -> Unit,
    done: () -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val scroll = rememberScrollState()
    var topReq by remember { mutableIntStateOf(0) }
    val handler = remember { Handler(Looper.getMainLooper()) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    val focus = remember(sentences.size) { Array(sentences.size) { FocusRequester() } }

    LaunchedEffect(scroll.isScrollInProgress) {
        if (scroll.isScrollInProgress) focusManager.clearFocus()
    }

    LaunchedEffect(topReq) {
        if (topReq > 0) scroll.scrollTo(0)
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        try {
            player?.stop()
        } catch (_: Exception) {
        }
        try {
            player?.release()
        } catch (_: Exception) {
        }
        player = null
    }

    fun play(i: Int, startMs: Int, endMs: Int) {
        stop()

        if (audioResId == 0) return

        val currentPlayer = MediaPlayer.create(context, audioResId) ?: return
        player = currentPlayer
        currentPlayer.setVolume(1f, 1f)
        currentPlayer.seekTo(startMs)
        currentPlayer.start()

        handler.postDelayed(
            {
                if (player == currentPlayer) {
                    stop()
                    focus[i].requestFocus()
                    keyboard?.show()
                }
            },
            (endMs - startMs).toLong()
        )
    }

    fun buildReport(currentScore: Int, correctionsSubmitted: Boolean): String {
        val builder = StringBuilder()
        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Submitted at: ").append(currentDateTimeText()).append("\n")
        builder.append("Lesson: ").append(name).append("\n")
        builder.append("Homework: Listening homework\n")
        builder.append("Original score: ").append(currentScore).append(" / ").append(sentences.size).append("\n")
        builder.append("Student submitted answers: yes\n")
        builder.append("Student submitted corrections: ").append(if (correctionsSubmitted) "yes" else "no").append("\n")

        var i = 0
        while (i < sentences.size) {
            builder.append(sentences[i].label)
            builder.append(": plays = ")
            builder.append(plays[i])
            builder.append(", hints = ")
            builder.append(hints[i])
            builder.append("\n")
            i++
        }

        return builder.toString()
    }

    fun submitAnswers() {
        stop()
        focusManager.clearFocus()

        var currentScore = 0
        var i = 0

        while (i < sentences.size) {
            val ok = isCorrectAnswer(answers[i], sentences[i].correctText)
            firstCorrect[i] = ok
            if (ok) currentScore++
            i++
        }

        setScore(currentScore)
        setStep(1)
        setMsg("First attempt score: $currentScore / ${sentences.size}")
        setReport(buildReport(currentScore, false))
        onAttemptChanged()
        topReq++
    }

    fun submitCorrections() {
        stop()
        focusManager.clearFocus()

        var allCorrect = true
        var i = 0

        while (i < sentences.size) {
            if (!isCorrectAnswer(answers[i], sentences[i].correctText)) allCorrect = false
            i++
        }

        if (allCorrect) {
            val finalReport = buildReport(score, true)

            setMsg("Sending your lesson report to your teacher...")
            setReport(finalReport)

            sendHomeworkReportToTeacher(
                studentName = studentName,
                lessonName = "Lesson 3",
                homeworkType = "Listening homework",
                scoreText = "$score / ${sentences.size}",
                report = finalReport
            ) { success ->
                Handler(Looper.getMainLooper()).post {
                    if (success) {
                        setStep(2)
                        done()
                        setMsg("Report sent successfully.")
                    } else {
                        setMsg("Failed to send report. Please try again.")
                    }
                }
            }
        } else {
            setMsg("Some corrections are still incorrect. Please check the red X sentences.")
            setReport(buildReport(score, false))
            onAttemptChanged()
        }

        topReq++
    }

    DisposableEffect(Unit) {
        onDispose { stop() }
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name, "Listening")

        if (msg != "") {
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.Black, Offset(1f, 1f), 5f)
                )
            )
        }

        if (audioResId == 0) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Audio file lesson2.mp3 not found in res/raw.",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        var i = 0
        while (i < sentences.size) {
            val index = i
            val sentence = sentences[index]

            SentenceRow(
                sentence = sentence,
                answer = answers[index],
                change = {
                    answers[index] = it
                    onAttemptChanged()
                },
                playCount = plays[index],
                submitStep = submitStep,
                firstOk = firstCorrect[index],
                currentOk = isCorrectAnswer(answers[index], sentence.correctText),
                hintCount = hints[index],
                focus = focus[index],
                play = {
                    plays[index]++
                    onAttemptChanged()
                    play(index, sentence.startMs, sentence.endMs)
                },
                stop = {
                    stop()
                },
                hint = {
                    if (canUseHint(answers[index], plays[index], submitStep >= 1, hints[index], sentence.correctText)) {
                        hints[index]++
                        setReport(buildReport(score, submitStep == 2))
                        onAttemptChanged()
                    }
                }
            )

            Spacer(Modifier.height(12.dp))
            i++
        }

        if (submitStep == 0) {
            ArtButton("Submit answers", { submitAnswers() })
        } else if (submitStep == 1) {
            ArtButton("Submit corrections", { submitCorrections() })
        } else {
            Button(
                onClick = { },
                enabled = false,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFF2E7D32),
                    disabledContentColor = Color.White
                )
            ) {
                Text("Submitted")
            }
        }

        Spacer(Modifier.height(16.dp))

        ArtButton(
            text = "Back",
            onClick = {
                stop()
                back()
            },
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}

@Composable
fun SentenceRow(
    sentence: HomeworkSentence,
    answer: String,
    change: (String) -> Unit,
    playCount: Int,
    submitStep: Int,
    firstOk: Boolean,
    currentOk: Boolean,
    hintCount: Int,
    focus: FocusRequester,
    play: () -> Unit,
    stop: () -> Unit,
    hint: () -> Unit
) {
    Card(
        Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row {
                Text(
                    text = sentence.label,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                if (submitStep >= 1) {
                    Text(
                        text = if (submitStep == 2 || currentOk) "✓" else "✕",
                        color = if (submitStep == 2 || currentOk) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 64.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row {
                ArtButton(
                    text = "▶",
                    onClick = play,
                    modifier = Modifier.width(90.dp),
                    heightDp = 56,
                    fontSize = 20
                )

                Spacer(Modifier.width(8.dp))

                ArtButton(
                    text = "Stop",
                    onClick = stop,
                    modifier = Modifier.width(112.dp),
                    backgroundResId = R.drawable.redbutton,
                    heightDp = 56,
                    fontSize = 16
                )
            }

            Spacer(Modifier.height(8.dp))

            TextField(
                value = answer,
                onValueChange = change,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus)
            )

            Spacer(Modifier.height(4.dp))

            Text("Plays: $playCount")

            if (submitStep >= 1) {
                Text(if (firstOk) "First attempt: correct" else "First attempt: incorrect")

                if (cleanAnswer(answer) == "") {
                    Text("Type an answer before using hints.")
                } else if (playCount < 5) {
                    Text("Hint locked: listen ${5 - playCount} more time(s).")
                } else if (submitStep < 2 && !currentOk) {
                    ArtButton(
                        text = "Reveal next word",
                        onClick = hint,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        backgroundResId = R.drawable.graybutton,
                        heightDp = 56,
                        fontSize = 16
                    )
                }

                if (hintCount > 0) {
                    Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}")
                    Text("Hints used: $hintCount")
                }
            }
        }
    }
}
