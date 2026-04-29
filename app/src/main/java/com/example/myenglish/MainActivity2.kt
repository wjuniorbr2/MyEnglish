package com.example.myenglish

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.ui.theme.MyEnglishTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.StringTokenizer

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyEnglishTheme {
                NewAppWithSplash()
            }
        }
    }
}

private object NewLesson1Audio {
    val AUDIO_RES_ID = R.raw.lesson1

    val sentences = arrayOf(
        HomeworkSentence("Sentence 1", "I like.", 870, 2527),
        HomeworkSentence("Sentence 2", "You eat meat.", 3750, 5880),
        HomeworkSentence("Sentence 3", "I don't like.", 7610, 9426),
        HomeworkSentence("Sentence 4", "They don't drink water.", 11140, 13257),
        HomeworkSentence("Sentence 5", "Do you drink milk?", 15400, 17676),
        HomeworkSentence("Sentence 6", "Do we eat meat?", 19230, 21388),
        HomeworkSentence("Sentence 7", "I like bread with butter.", 23320, 26212),
        HomeworkSentence("Sentence 8", "I eat.", 28140, 29943),
        HomeworkSentence("Sentence 9", "You like.", 31790, 33424),
        HomeworkSentence("Sentence 10", "I don't like.", 35040, 37255),
        HomeworkSentence("Sentence 11", "You don't like.", 38820, 40794),
        HomeworkSentence("Sentence 12", "Do you like?", 42610, 44612),
        HomeworkSentence("Sentence 13", "Do they like?", 46060, 48280),
        HomeworkSentence("Sentence 14", "Don't you like?", 50060, 53014),
        HomeworkSentence("Sentence 15", "Don't they like?", 54040, 56920),
        HomeworkSentence("Sentence 16", "I like to eat.", 58100, 60369),
        HomeworkSentence("Sentence 17", "I eat in the morning.", 61660, 64021),
        HomeworkSentence("Sentence 18", "I drink milk in the morning.", 65550, 68597),
        HomeworkSentence("Sentence 19", "Do you like milk?", 70140, 72258),
        HomeworkSentence("Sentence 20", "They don't eat bread with butter.", 74010, 77491),
        HomeworkSentence("Sentence 21", "I don't like to eat in the morning.", 79160, 82187),
        HomeworkSentence("Sentence 22", "I drink water.", 83690, 85800),
        HomeworkSentence("Sentence 23", "I eat bread.", 87270, 89614),
        HomeworkSentence("Sentence 24", "I drink juice.", 91220, 93297),
        HomeworkSentence("Sentence 25", "I don't like milk.", 95200, 97238),
        HomeworkSentence("Sentence 26", "I don't drink in the morning.", 99010, 101591),
        HomeworkSentence("Sentence 27", "You eat.", 103310, 104970),
        HomeworkSentence("Sentence 28", "We like.", 106630, 108521),
        HomeworkSentence("Sentence 29", "They like to drink at night.", 110110, 112936),
        HomeworkSentence("Sentence 30", "We don't drink in the morning.", 114640, 116940),
        HomeworkSentence("Sentence 31", "Do you like butter?", 118630, 120577),
        HomeworkSentence("Sentence 32", "Do they like to drink beer?", 122320, 124916)
    )
}

@Composable
fun NewAppWithSplash() {
    var splashVisible by remember { mutableStateOf(true) }
    var splashAlphaTarget by remember { mutableStateOf(0f) }
    val splashAlpha by animateFloatAsState(targetValue = splashAlphaTarget, label = "splashAlpha")

    DisposableEffect(Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({ splashAlphaTarget = 1f }, 100)
        handler.postDelayed({ splashAlphaTarget = 0f }, 2100)
        handler.postDelayed({ splashVisible = false }, 3100)
        onDispose { handler.removeCallbacksAndMessages(null) }
    }

    if (splashVisible) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.appsplash),
                contentDescription = "My English splash",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .alpha(splashAlpha),
                contentScale = ContentScale.Fit
            )
        }
    } else {
        NewScreenBackground {
            NewLessonListScreen()
        }
    }
}

@Composable
fun NewScreenBackground(content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.screenbg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        content()
    }
}

@Composable
fun NewTitleText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 34,
    textAlign: TextAlign = TextAlign.Start
) {
    Box(
        modifier = modifier
            .background(Color(0x6607142F), RoundedCornerShape(18.dp))
            .border(1.dp, Color(0x88FFFFFF), RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = Color(0xFF032449),
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Black,
            textAlign = textAlign,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(Color(0xFF00E5FF), Offset(0f, 0f), 16f)
            ),
            modifier = Modifier.offset(x = 2.dp, y = 2.dp)
        )
        Text(
            text = text,
            color = Color.White,
            fontSize = fontSize.sp,
            fontWeight = FontWeight.Black,
            textAlign = textAlign,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(Color(0xFFFF4FA3), Offset(-1f, 1f), 9f)
            )
        )
    }
}

@Composable
fun NewImageButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundResId: Int = R.drawable.bluebutton,
    enabled: Boolean = true,
    fontSize: Int = 18,
    heightDp: Int = 76,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .height(heightDp.dp)
            .clip(RoundedCornerShape(18.dp))
            .alpha(if (enabled) 1f else 0.58f)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = backgroundResId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(heightDp.dp),
            contentScale = ContentScale.Crop
        )

        if (content == null) {
            Text(
                text = text,
                color = Color.White,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.Black, Offset(1.5f, 1.5f), 6f)
                )
            )
        } else {
            content()
        }
    }
}

@Composable
fun NewHomeworkCircleIcon(kind: Int, done: Boolean) {
    val iconColor = if (done) {
        if (kind == 0) Color(0xFF00AEEF) else if (kind == 1) Color(0xFFFFB300) else Color(0xFFE91E63)
    } else {
        Color(0xFF8E8E8E)
    }

    Canvas(modifier = Modifier.size(40.dp)) {
        val w = size.width
        val h = size.height
        drawCircle(Color.White, radius = w * 0.48f, center = Offset(w / 2f, h / 2f))
        drawCircle(Color.Black, radius = w * 0.48f, center = Offset(w / 2f, h / 2f), style = Stroke(width = 2.4f))

        if (kind == 0) {
            drawArc(iconColor, 205f, 130f, false, Offset(w * 0.24f, h * 0.20f), Size(w * 0.52f, h * 0.52f), style = Stroke(width = 3.4f))
            drawRoundRect(iconColor, Offset(w * 0.22f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
            drawRoundRect(iconColor, Offset(w * 0.64f, h * 0.50f), Size(w * 0.14f, h * 0.24f), CornerRadius(4f, 4f))
        } else if (kind == 1) {
            drawRoundRect(iconColor, Offset(w * 0.25f, h * 0.23f), Size(w * 0.50f, h * 0.56f), CornerRadius(5f, 5f), style = Stroke(width = 3f))
            drawLine(iconColor, Offset(w * 0.35f, h * 0.38f), Offset(w * 0.66f, h * 0.38f), strokeWidth = 2.4f)
            drawLine(iconColor, Offset(w * 0.35f, h * 0.52f), Offset(w * 0.66f, h * 0.52f), strokeWidth = 2.4f)
            drawLine(iconColor, Offset(w * 0.35f, h * 0.66f), Offset(w * 0.56f, h * 0.66f), strokeWidth = 2.4f)
            drawLine(iconColor, Offset(w * 0.25f, h * 0.30f), Offset(w * 0.25f, h * 0.72f), strokeWidth = 4f)
        } else {
            drawRoundRect(iconColor, Offset(w * 0.38f, h * 0.18f), Size(w * 0.24f, h * 0.42f), CornerRadius(10f, 10f), style = Stroke(width = 3.2f))
            drawArc(iconColor, 25f, 130f, false, Offset(w * 0.25f, h * 0.40f), Size(w * 0.50f, h * 0.28f), style = Stroke(width = 3f))
            drawLine(iconColor, Offset(w * 0.50f, h * 0.62f), Offset(w * 0.50f, h * 0.78f), strokeWidth = 3f)
            drawLine(iconColor, Offset(w * 0.34f, h * 0.80f), Offset(w * 0.66f, h * 0.80f), strokeWidth = 3f)
        }
    }
}

@Composable
fun NewLessonListScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", Context.MODE_PRIVATE) }
    val lessonSentences = NewLesson1Audio.sentences

    var currentScreen by remember { mutableStateOf("home") }
    var selectedLesson by remember { mutableStateOf("Lesson 1") }
    var showHomeworkChoices by remember { mutableStateOf(false) }
    var studentName by remember { mutableStateOf(prefs.getString("student_name", "") ?: "") }
    var showNameDialog by remember { mutableStateOf(studentName == "") }
    var lesson1ListeningDone by remember { mutableStateOf(prefs.getBoolean("lesson1_listening_done", false)) }

    val answers = remember { mutableStateListOf<String>() }
    val replayCounts = remember { mutableStateListOf<Int>() }
    val hintCounts = remember { mutableStateListOf<Int>() }
    val firstAttemptCorrect = remember { mutableStateListOf<Boolean>() }

    fun resetHomeworkAttempt() {
        answers.clear(); replayCounts.clear(); hintCounts.clear(); firstAttemptCorrect.clear()
        var index = 0
        while (index < lessonSentences.size) {
            answers.add(""); replayCounts.add(0); hintCounts.add(0); firstAttemptCorrect.add(false)
            index = index + 1
        }
    }

    LaunchedEffect(Unit) { resetHomeworkAttempt() }

    var submitStep by remember { mutableIntStateOf(0) }
    var firstAttemptScore by remember { mutableIntStateOf(0) }
    var submittedMessage by remember { mutableStateOf("") }
    var teacherReportToSend by remember { mutableStateOf("") }

    fun startNewListeningAttemptIfNeeded() {
        if (submitStep == 2) {
            resetHomeworkAttempt()
            submitStep = 0; firstAttemptScore = 0; submittedMessage = ""; teacherReportToSend = ""
        }
    }

    BackHandler(enabled = currentScreen != "home") {
        if (currentScreen == "homework") currentScreen = "lesson" else if (currentScreen == "lesson") currentScreen = "home"
    }

    if (showNameDialog) {
        StudentNameDialog(currentName = studentName, onSave = { newName ->
            studentName = newName
            prefs.edit().putString("student_name", newName).apply()
            showNameDialog = false
        })
    }

    when (currentScreen) {
        "home" -> NewHomeScreen(displayStudentName(studentName), { showNameDialog = true }, { lessonName ->
            selectedLesson = lessonName; showHomeworkChoices = false; currentScreen = "lesson"
        })
        "lesson" -> NewLessonScreen(selectedLesson, lesson1ListeningDone, showHomeworkChoices, { showHomeworkChoices = true }, {
            if (selectedLesson == "Lesson 1") { startNewListeningAttemptIfNeeded(); currentScreen = "homework" }
        }, { currentScreen = "home" })
        "homework" -> NewHomeworkScreen(
            selectedLesson, displayStudentName(studentName), lessonSentences, answers, replayCounts, hintCounts, firstAttemptCorrect,
            submitStep, firstAttemptScore, submittedMessage,
            { firstAttemptScore = it }, { submitStep = it }, { submittedMessage = it }, { teacherReportToSend = it },
            { lesson1ListeningDone = true; prefs.edit().putBoolean("lesson1_listening_done", true).apply() },
            { currentScreen = "lesson" }
        )
    }
}

@Composable
fun NewHomeScreen(studentName: String, onChangeName: () -> Unit, onOpenLesson: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Column(Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(Modifier.weight(1f)) {
                NewTitleText("My English", fontSize = 36)
                Spacer(Modifier.height(6.dp))
                NewTitleText("Book 1", fontSize = 24)
            }
            Text(studentName, modifier = Modifier.clickable { onChangeName() }, color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(18.dp))
        var lessonNumber = 1
        while (lessonNumber <= 31) {
            val n = lessonNumber
            NewImageButton("Lesson $n", { onOpenLesson("Lesson $n") }, Modifier.fillMaxWidth())
            Spacer(Modifier.height(8.dp))
            lessonNumber = lessonNumber + 1
        }
    }
}

@Composable
fun NewLessonScreen(
    lessonName: String,
    lesson1ListeningDone: Boolean,
    showHomeworkChoices: Boolean,
    onShowHomeworkChoices: () -> Unit,
    onOpenListeningHomework: () -> Unit,
    onBack: () -> Unit
) {
    val listeningAvailable = lessonName == "Lesson 1"
    val listeningDoneForThisLesson = lesson1ListeningDone && lessonName == "Lesson 1"

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        NewTitleText(lessonName, fontSize = 34)
        Spacer(Modifier.height(16.dp))
        NewImageButton("Vocabulary", { }, Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        NewImageButton("Grammar", { }, Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        NewImageButton("Practice", { }, Modifier.fillMaxWidth())
        Spacer(Modifier.height(8.dp))
        NewImageButton("Homework", onShowHomeworkChoices, Modifier.fillMaxWidth(), content = {
            Text("Homework", modifier = Modifier.align(Alignment.Center), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center, style = LocalTextStyle.current.copy(shadow = Shadow(Color.Black, Offset(1.5f, 1.5f), 6f)))
            Row(Modifier.align(Alignment.CenterEnd).padding(end = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                NewHomeworkCircleIcon(0, listeningDoneForThisLesson)
                Spacer(Modifier.width(5.dp))
                NewHomeworkCircleIcon(1, false)
                Spacer(Modifier.width(5.dp))
                NewHomeworkCircleIcon(2, false)
            }
        })

        if (showHomeworkChoices) {
            Spacer(Modifier.height(16.dp))
            NewTitleText("Choose homework", fontSize = 22)
            Spacer(Modifier.height(8.dp))
            Box(Modifier.fillMaxWidth()) {
                NewImageButton(if (listeningAvailable) "Listening homework" else "Listening homework - coming soon", onOpenListeningHomework, Modifier.fillMaxWidth(), enabled = listeningAvailable)
                if (listeningDoneForThisLesson) {
                    Image(painterResource(id = R.drawable.donestamp), "Done", Modifier.align(Alignment.CenterEnd).size(76.dp))
                }
            }
            Spacer(Modifier.height(8.dp))
            NewImageButton("Written homework", { }, Modifier.fillMaxWidth(), backgroundResId = R.drawable.graybutton, enabled = false)
            Spacer(Modifier.height(8.dp))
            NewImageButton("Spoken homework", { }, Modifier.fillMaxWidth(), backgroundResId = R.drawable.graybutton, enabled = false)
        }
        Spacer(Modifier.height(16.dp))
        NewImageButton("Back", onBack, Modifier.fillMaxWidth(), backgroundResId = R.drawable.redbutton)
    }
}

@Composable
fun NewHomeworkScreen(
    lessonName: String,
    studentName: String,
    lessonSentences: Array<HomeworkSentence>,
    answers: MutableList<String>,
    replayCounts: MutableList<Int>,
    hintCounts: MutableList<Int>,
    firstAttemptCorrect: MutableList<Boolean>,
    submitStep: Int,
    firstAttemptScore: Int,
    submittedMessage: String,
    onFirstAttemptScoreChange: (Int) -> Unit,
    onSubmitStepChange: (Int) -> Unit,
    onSubmittedMessageChange: (String) -> Unit,
    onTeacherReportChange: (String) -> Unit,
    onListeningDone: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    var scrollToTopRequest by remember { mutableIntStateOf(0) }
    val handler = remember { Handler(Looper.getMainLooper()) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val focusRequesters = remember { Array(lessonSentences.size) { FocusRequester() } }

    LaunchedEffect(scrollState.isScrollInProgress) { if (scrollState.isScrollInProgress) focusManager.clearFocus() }
    LaunchedEffect(scrollToTopRequest) { if (scrollToTopRequest > 0) scrollState.scrollTo(0) }

    fun stopAudio() {
        handler.removeCallbacksAndMessages(null)
        try { mediaPlayer?.stop() } catch (_: Exception) { }
        try { mediaPlayer?.release() } catch (_: Exception) { }
        mediaPlayer = null
    }

    fun playAudioPart(sentenceIndex: Int, startMs: Int, endMs: Int) {
        stopAudio()
        val player = MediaPlayer.create(context, NewLesson1Audio.AUDIO_RES_ID) ?: return
        mediaPlayer = player
        player.setVolume(1.0f, 1.0f)
        player.seekTo(startMs)
        player.start()
        handler.postDelayed({
            if (mediaPlayer == player) {
                stopAudio()
                focusRequesters[sentenceIndex].requestFocus()
                keyboardController?.show()
            }
        }, (endMs - startMs).toLong())
    }

    fun buildTeacherReport(score: Int, correctionsSubmitted: Boolean): String {
        val builder = StringBuilder()
        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Submitted at: ").append(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())).append("\n")
        builder.append("Lesson: ").append(lessonName).append("\n")
        builder.append("Homework: Listening homework\n")
        builder.append("Original score: ").append(score).append(" / ").append(lessonSentences.size).append("\n")
        builder.append("Student submitted answers: yes\n")
        builder.append("Student submitted corrections: ").append(if (correctionsSubmitted) "yes" else "no").append("\n")
        var index = 0
        while (index < lessonSentences.size) {
            builder.append(lessonSentences[index].label).append(": plays = ").append(replayCounts[index]).append(", hints = ").append(hintCounts[index]).append("\n")
            index = index + 1
        }
        return builder.toString()
    }

    fun submitAnswers() {
        stopAudio(); focusManager.clearFocus()
        var score = 0; var index = 0
        while (index < lessonSentences.size) {
            val correct = isCorrectAnswer(answers[index], lessonSentences[index].correctText)
            firstAttemptCorrect[index] = correct
            if (correct) score = score + 1
            index = index + 1
        }
        onFirstAttemptScoreChange(score)
        onSubmitStepChange(1)
        onSubmittedMessageChange("First attempt score: $score / ${lessonSentences.size}")
        onTeacherReportChange(buildTeacherReport(score, false))
        scrollToTopRequest = scrollToTopRequest + 1
    }

    fun submitCorrections() {
        stopAudio(); focusManager.clearFocus()
        var allCorrect = true; var index = 0
        while (index < lessonSentences.size) {
            if (!isCorrectAnswer(answers[index], lessonSentences[index].correctText)) allCorrect = false
            index = index + 1
        }
        if (allCorrect) {
            onSubmitStepChange(2)
            onSubmittedMessageChange("Submitted! All corrections are correct.")
            onTeacherReportChange(buildTeacherReport(firstAttemptScore, true))
            onListeningDone()
        } else {
            onSubmittedMessageChange("Some corrections are still incorrect. Please check the red X sentences.")
            onTeacherReportChange(buildTeacherReport(firstAttemptScore, false))
        }
        scrollToTopRequest = scrollToTopRequest + 1
    }

    DisposableEffect(Unit) { onDispose { stopAudio() } }

    Column(Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)) {
        NewTitleText("$lessonName - Listening homework", fontSize = 24)
        if (submittedMessage != "") {
            Spacer(Modifier.height(8.dp))
            Text(submittedMessage, color = Color.White, fontWeight = FontWeight.Bold, style = LocalTextStyle.current.copy(shadow = Shadow(Color.Black, Offset(1f, 1f), 5f)))
        }
        Spacer(Modifier.height(16.dp))
        var rowIndex = 0
        while (rowIndex < lessonSentences.size) {
            val i = rowIndex
            val sentence = lessonSentences[i]
            NewSentenceAnswerRow(sentence, answers[i], { answers[i] = it }, replayCounts[i], submitStep, firstAttemptCorrect[i], isCorrectAnswer(answers[i], sentence.correctText), hintCounts[i], focusRequesters[i], {
                replayCounts[i] = replayCounts[i] + 1
                playAudioPart(i, sentence.startMs, sentence.endMs)
            }, { stopAudio() }, {
                if (canUseHint(answers[i], replayCounts[i], submitStep >= 1, hintCounts[i], sentence.correctText)) {
                    hintCounts[i] = hintCounts[i] + 1
                    onTeacherReportChange(buildTeacherReport(firstAttemptScore, submitStep == 2))
                }
            })
            Spacer(Modifier.height(12.dp))
            rowIndex = rowIndex + 1
        }
        if (submitStep == 0) NewImageButton("Submit answers", { submitAnswers() }, Modifier.fillMaxWidth())
        else if (submitStep == 1) NewImageButton("Submit corrections", { submitCorrections() }, Modifier.fillMaxWidth())
        else Button(onClick = { }, enabled = false, colors = ButtonDefaults.buttonColors(disabledContainerColor = Color(0xFF2E7D32), disabledContentColor = Color.White)) { Text("Submitted") }
        Spacer(Modifier.height(16.dp))
        NewImageButton("Back", { stopAudio(); onBack() }, Modifier.fillMaxWidth(), backgroundResId = R.drawable.redbutton)
    }
}

@Composable
fun NewSentenceAnswerRow(
    sentence: HomeworkSentence,
    answer: String,
    onAnswerChange: (String) -> Unit,
    replayCount: Int,
    submitStep: Int,
    firstAttemptCorrect: Boolean,
    currentCorrect: Boolean,
    hintCount: Int,
    focusRequester: FocusRequester,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    onHint: () -> Unit
) {
    Card(Modifier.fillMaxWidth(), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row {
                Text(sentence.label, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                if (submitStep >= 1) Text(if (submitStep == 2 || currentCorrect) "✓" else "✕", color = if (submitStep == 2 || currentCorrect) Color(0xFF2E7D32) else Color(0xFFC62828), fontSize = 64.sp)
            }
            Spacer(Modifier.height(8.dp))
            Row {
                NewImageButton("▶", onPlay, Modifier.width(90.dp), heightDp = 64, fontSize = 20)
                Spacer(Modifier.width(8.dp))
                NewImageButton("Stop", onStop, Modifier.width(112.dp), backgroundResId = R.drawable.redbutton, heightDp = 64, fontSize = 16)
            }
            Spacer(Modifier.height(8.dp))
            TextField(answer, onAnswerChange, Modifier.fillMaxWidth().focusRequester(focusRequester))
            Spacer(Modifier.height(4.dp))
            Text("Plays: $replayCount")
            if (submitStep >= 1) {
                Text(if (firstAttemptCorrect) "First attempt: correct" else "First attempt: incorrect")
                if (cleanAnswer(answer) == "") Text("Type an answer before using hints.")
                else if (replayCount < 5) Text("Hint locked: listen ${5 - replayCount} more time(s).")
                else if (submitStep < 2 && !currentCorrect) NewImageButton("Reveal next word", onHint, Modifier.fillMaxWidth(), backgroundResId = R.drawable.graybutton, heightDp = 64, fontSize = 16)
                if (hintCount > 0) {
                    Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}")
                    Text("Hints used: $hintCount")
                }
            }
        }
    }
}
