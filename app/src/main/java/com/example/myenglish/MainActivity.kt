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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyEnglishTheme {
                AppWithSplash()
            }
        }
    }
}

data class HomeworkSentence(
    val label: String,
    val correctText: String,
    val startMs: Int,
    val endMs: Int
)

private object Lesson1Homework1Audio {
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

fun cleanAnswer(text: String): String {
    val builder = StringBuilder()
    var lastWasSpace = true
    var i = 0

    while (i < text.length) {
        val currentChar = text[i]
        val lowerChar = Character.toLowerCase(currentChar)

        if (lowerChar >= 'a' && lowerChar <= 'z') {
            builder.append(lowerChar)
            lastWasSpace = false
        } else if (lowerChar >= '0' && lowerChar <= '9') {
            builder.append(lowerChar)
            lastWasSpace = false
        } else if (currentChar == ' ' || currentChar == '\n' || currentChar == '\t') {
            if (!lastWasSpace && builder.length > 0) {
                builder.append(' ')
                lastWasSpace = true
            }
        }

        i = i + 1
    }

    if (builder.length > 0 && builder[builder.length - 1] == ' ') {
        builder.deleteCharAt(builder.length - 1)
    }

    return builder.toString()
}

fun isCorrectAnswer(studentAnswer: String, correctAnswer: String): Boolean {
    return cleanAnswer(studentAnswer) == cleanAnswer(correctAnswer)
}

fun countWords(text: String): Int {
    val cleanText = cleanAnswer(text)
    if (cleanText == "") return 0
    val tokenizer = StringTokenizer(cleanText, " ")
    return tokenizer.countTokens()
}

fun revealedHintText(correctText: String, hintCount: Int): String {
    val cleanText = cleanAnswer(correctText)
    if (cleanText == "" || hintCount <= 0) return ""

    val tokenizer = StringTokenizer(cleanText, " ")
    val builder = StringBuilder()
    var wordsAdded = 0

    while (tokenizer.hasMoreTokens() && wordsAdded < hintCount) {
        if (builder.length > 0) builder.append(" ")
        builder.append(tokenizer.nextToken())
        wordsAdded = wordsAdded + 1
    }

    return builder.toString()
}

fun canUseHint(
    answer: String,
    replayCount: Int,
    submittedAnswers: Boolean,
    hintCount: Int,
    correctText: String
): Boolean {
    return submittedAnswers &&
        cleanAnswer(answer) != "" &&
        replayCount >= 5 &&
        hintCount < countWords(correctText)
}

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
        if (secondName.length > 0) {
            return firstName + " " + Character.toUpperCase(secondName[0]) + "."
        }
    }

    return firstName
}

@Composable
fun ScreenBackground(content: @Composable () -> Unit) {
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
fun AppTitleText(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Int = 34,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        modifier = modifier,
        color = Color.White,
        fontSize = fontSize.sp,
        fontWeight = FontWeight.Black,
        textAlign = textAlign,
        style = LocalTextStyle.current.copy(
            shadow = Shadow(
                color = Color(0xFF00B8FF),
                offset = Offset(0f, 0f),
                blurRadius = 18f
            )
        )
    )
}

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundResId: Int = R.drawable.bluebutton,
    enabled: Boolean = true,
    fontSize: Int = 18,
    content: (@Composable BoxScope.() -> Unit)? = null
) {
    Box(
        modifier = modifier
            .height(54.dp)
            .alpha(if (enabled) 1f else 0.55f)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = backgroundResId),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )

        if (content == null) {
            Text(
                text = text,
                color = Color.White,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(1.5f, 1.5f),
                        blurRadius = 5f
                    )
                )
            )
        } else {
            content()
        }
    }
}

@Composable
fun HomeworkStatusIcon(kind: Int, done: Boolean) {
    val iconColor = if (done) {
        if (kind == 0) Color(0xFF00D9FF) else if (kind == 1) Color(0xFFFFD54F) else Color(0xFFFF5FD2)
    } else {
        Color(0xFFB8B8B8)
    }

    Canvas(modifier = Modifier.size(27.dp)) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 3.2f)

        if (kind == 0) {
            drawArc(
                color = iconColor,
                startAngle = 205f,
                sweepAngle = 130f,
                useCenter = false,
                topLeft = Offset(w * 0.18f, h * 0.16f),
                size = Size(w * 0.64f, h * 0.62f),
                style = stroke
            )
            drawLine(iconColor, Offset(w * 0.24f, h * 0.54f), Offset(w * 0.24f, h * 0.82f), strokeWidth = 4f)
            drawLine(iconColor, Offset(w * 0.76f, h * 0.54f), Offset(w * 0.76f, h * 0.82f), strokeWidth = 4f)
        } else if (kind == 1) {
            drawLine(iconColor, Offset(w * 0.22f, h * 0.78f), Offset(w * 0.72f, h * 0.28f), strokeWidth = 4f)
            drawLine(iconColor, Offset(w * 0.67f, h * 0.23f), Offset(w * 0.80f, h * 0.36f), strokeWidth = 4f)
            drawLine(iconColor, Offset(w * 0.18f, h * 0.84f), Offset(w * 0.36f, h * 0.80f), strokeWidth = 3f)
        } else {
            drawOval(
                color = iconColor,
                topLeft = Offset(w * 0.34f, h * 0.14f),
                size = Size(w * 0.32f, h * 0.46f),
                style = stroke
            )
            drawLine(iconColor, Offset(w * 0.50f, h * 0.60f), Offset(w * 0.50f, h * 0.82f), strokeWidth = 3.2f)
            drawLine(iconColor, Offset(w * 0.34f, h * 0.84f), Offset(w * 0.66f, h * 0.84f), strokeWidth = 3.2f)
            drawArc(
                color = iconColor,
                startAngle = 25f,
                sweepAngle = 130f,
                useCenter = false,
                topLeft = Offset(w * 0.22f, h * 0.42f),
                size = Size(w * 0.56f, h * 0.30f),
                style = stroke
            )
        }
    }
}

@Composable
fun AppWithSplash() {
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

    Box(modifier = Modifier.fillMaxSize()) {
        ScreenBackground {
            LessonListScreen()
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
        }
    }
}

@Composable
fun LessonListScreen() {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", Context.MODE_PRIVATE) }
    val lessonSentences = Lesson1Homework1Audio.sentences

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
        answers.clear()
        replayCounts.clear()
        hintCounts.clear()
        firstAttemptCorrect.clear()

        var index = 0
        while (index < lessonSentences.size) {
            answers.add("")
            replayCounts.add(0)
            hintCounts.add(0)
            firstAttemptCorrect.add(false)
            index = index + 1
        }
    }

    LaunchedEffect(Unit) {
        resetHomeworkAttempt()
    }

    var submitStep by remember { mutableIntStateOf(0) }
    var firstAttemptScore by remember { mutableIntStateOf(0) }
    var submittedMessage by remember { mutableStateOf("") }
    var teacherReportToSend by remember { mutableStateOf("") }

    fun startNewListeningAttemptIfNeeded() {
        if (submitStep == 2) {
            resetHomeworkAttempt()
            submitStep = 0
            firstAttemptScore = 0
            submittedMessage = ""
            teacherReportToSend = ""
        }
    }

    BackHandler(enabled = currentScreen != "home") {
        if (currentScreen == "homework") {
            currentScreen = "lesson"
        } else if (currentScreen == "lesson") {
            currentScreen = "home"
        }
    }

    if (showNameDialog) {
        StudentNameDialog(
            currentName = studentName,
            onSave = { newName ->
                studentName = newName
                prefs.edit().putString("student_name", newName).apply()
                showNameDialog = false
            }
        )
    }

    when (currentScreen) {
        "home" -> {
            HomeScreen(
                studentName = displayStudentName(studentName),
                onChangeName = { showNameDialog = true },
                onOpenLesson = { lessonName ->
                    selectedLesson = lessonName
                    showHomeworkChoices = false
                    currentScreen = "lesson"
                }
            )
        }

        "lesson" -> {
            LessonScreen(
                lessonName = selectedLesson,
                lesson1ListeningDone = lesson1ListeningDone,
                showHomeworkChoices = showHomeworkChoices,
                onShowHomeworkChoices = { showHomeworkChoices = true },
                onOpenListeningHomework = {
                    if (selectedLesson == "Lesson 1") {
                        startNewListeningAttemptIfNeeded()
                        currentScreen = "homework"
                    }
                },
                onBack = { currentScreen = "home" }
            )
        }

        "homework" -> {
            HomeworkScreen(
                lessonName = selectedLesson,
                studentName = displayStudentName(studentName),
                lessonSentences = lessonSentences,
                answers = answers,
                replayCounts = replayCounts,
                hintCounts = hintCounts,
                firstAttemptCorrect = firstAttemptCorrect,
                submitStep = submitStep,
                firstAttemptScore = firstAttemptScore,
                submittedMessage = submittedMessage,
                onFirstAttemptScoreChange = { firstAttemptScore = it },
                onSubmitStepChange = { submitStep = it },
                onSubmittedMessageChange = { submittedMessage = it },
                onTeacherReportChange = { teacherReportToSend = it },
                onListeningDone = {
                    lesson1ListeningDone = true
                    prefs.edit().putBoolean("lesson1_listening_done", true).apply()
                },
                onBack = { currentScreen = "lesson" }
            )
        }
    }
}

@Composable
fun StudentNameDialog(currentName: String, onSave: (String) -> Unit) {
    var nameText by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = { },
        title = { Text("Student name") },
        text = {
            TextField(
                value = nameText,
                onValueChange = { nameText = it },
                label = { Text("First name and initial") }
            )
        },
        confirmButton = {
            Button(onClick = {
                if (cleanAnswer(nameText) != "") {
                    onSave(nameText)
                }
            }) {
                Text("Save")
            }
        }
    )
}

@Composable
fun HomeScreen(
    studentName: String,
    onChangeName: () -> Unit,
    onOpenLesson: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
            Column(modifier = Modifier.weight(1f)) {
                AppTitleText("My English", fontSize = 38)
                AppTitleText("Book 1", fontSize = 28)
            }

            Text(
                text = studentName,
                modifier = Modifier.clickable { onChangeName() },
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.Black, Offset(1f, 1f), 5f)
                )
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        var lessonNumber = 1
        while (lessonNumber <= 31) {
            val currentLessonNumber = lessonNumber
            AppButton(
                text = "Lesson $currentLessonNumber",
                onClick = { onOpenLesson("Lesson $currentLessonNumber") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            lessonNumber = lessonNumber + 1
        }
    }
}

@Composable
fun LessonScreen(
    lessonName: String,
    lesson1ListeningDone: Boolean,
    showHomeworkChoices: Boolean,
    onShowHomeworkChoices: () -> Unit,
    onOpenListeningHomework: () -> Unit,
    onBack: () -> Unit
) {
    val listeningAvailable = lessonName == "Lesson 1"
    val listeningDoneForThisLesson = lesson1ListeningDone && lessonName == "Lesson 1"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AppTitleText(lessonName, fontSize = 36)

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(text = "Vocabulary", onClick = { }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        AppButton(text = "Grammar", onClick = { }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        AppButton(text = "Practice", onClick = { }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))

        AppButton(
            text = "Homework",
            onClick = onShowHomeworkChoices,
            modifier = Modifier.fillMaxWidth(),
            content = {
                Text(
                    text = "Homework",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = LocalTextStyle.current.copy(
                        shadow = Shadow(Color.Black, Offset(1.5f, 1.5f), 5f)
                    )
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HomeworkStatusIcon(0, listeningDoneForThisLesson)
                    Spacer(modifier = Modifier.width(9.dp))
                    HomeworkStatusIcon(1, false)
                    Spacer(modifier = Modifier.width(9.dp))
                    HomeworkStatusIcon(2, false)
                }
            }
        )

        if (showHomeworkChoices) {
            Spacer(modifier = Modifier.height(16.dp))
            AppTitleText("Choose homework", fontSize = 24)
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                AppButton(
                    text = if (listeningAvailable) "Listening homework" else "Listening homework - coming soon",
                    onClick = onOpenListeningHomework,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = listeningAvailable
                )

                if (listeningDoneForThisLesson) {
                    Image(
                        painter = painterResource(id = R.drawable.donestamp),
                        contentDescription = "Done",
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(72.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            AppButton(
                text = "Written homework",
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                backgroundResId = R.drawable.graybutton,
                enabled = false
            )
            Spacer(modifier = Modifier.height(8.dp))
            AppButton(
                text = "Spoken homework",
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                backgroundResId = R.drawable.graybutton,
                enabled = false
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "Back", onClick = onBack, modifier = Modifier.fillMaxWidth(), backgroundResId = R.drawable.redbutton)
    }
}

@Composable
fun HomeworkScreen(
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

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(scrollToTopRequest) {
        if (scrollToTopRequest > 0) {
            scrollState.scrollTo(0)
        }
    }

    fun stopAudio() {
        handler.removeCallbacksAndMessages(null)
        try {
            mediaPlayer?.stop()
        } catch (_: Exception) {
        }
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {
        }
        mediaPlayer = null
    }

    fun playAudioPart(sentenceIndex: Int, startMs: Int, endMs: Int) {
        stopAudio()

        val player = MediaPlayer.create(context, Lesson1Homework1Audio.AUDIO_RES_ID)
        if (player == null) return

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
        var index = 0

        builder.append("Student: ")
        builder.append(studentName)
        builder.append("\n")
        builder.append("Submitted at: ")
        builder.append(currentDateTimeText())
        builder.append("\n")
        builder.append("Lesson: ")
        builder.append(lessonName)
        builder.append("\n")
        builder.append("Homework: Listening homework\n")
        builder.append("Original score: ")
        builder.append(score)
        builder.append(" / ")
        builder.append(lessonSentences.size)
        builder.append("\n")
        builder.append("Student submitted answers: yes\n")
        builder.append("Student submitted corrections: ")
        builder.append(if (correctionsSubmitted) "yes" else "no")
        builder.append("\n")

        while (index < lessonSentences.size) {
            builder.append(lessonSentences[index].label)
            builder.append(": plays = ")
            builder.append(replayCounts[index])
            builder.append(", hints = ")
            builder.append(hintCounts[index])
            builder.append("\n")
            index = index + 1
        }

        return builder.toString()
    }

    fun submitAnswers() {
        stopAudio()
        focusManager.clearFocus()

        var score = 0
        var index = 0

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
        stopAudio()
        focusManager.clearFocus()

        var allCorrect = true
        var index = 0

        while (index < lessonSentences.size) {
            if (!isCorrectAnswer(answers[index], lessonSentences[index].correctText)) {
                allCorrect = false
            }
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

    DisposableEffect(Unit) {
        onDispose { stopAudio() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        AppTitleText("$lessonName - Listening homework", fontSize = 28)

        if (submittedMessage != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                submittedMessage,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.Black, Offset(1f, 1f), 4f)
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        var rowIndex = 0
        while (rowIndex < lessonSentences.size) {
            val currentIndex = rowIndex
            val currentSentence = lessonSentences[currentIndex]

            SentenceAnswerRow(
                sentence = currentSentence,
                answer = answers[currentIndex],
                onAnswerChange = { answers[currentIndex] = it },
                replayCount = replayCounts[currentIndex],
                submitStep = submitStep,
                firstAttemptCorrect = firstAttemptCorrect[currentIndex],
                currentCorrect = isCorrectAnswer(answers[currentIndex], currentSentence.correctText),
                hintCount = hintCounts[currentIndex],
                focusRequester = focusRequesters[currentIndex],
                onPlay = {
                    replayCounts[currentIndex] = replayCounts[currentIndex] + 1
                    playAudioPart(currentIndex, currentSentence.startMs, currentSentence.endMs)
                },
                onStop = { stopAudio() },
                onHint = {
                    if (canUseHint(
                            answers[currentIndex],
                            replayCounts[currentIndex],
                            submitStep >= 1,
                            hintCounts[currentIndex],
                            currentSentence.correctText
                        )
                    ) {
                        hintCounts[currentIndex] = hintCounts[currentIndex] + 1
                        onTeacherReportChange(buildTeacherReport(firstAttemptScore, submitStep == 2))
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            rowIndex = rowIndex + 1
        }

        if (submitStep == 0) {
            AppButton(text = "Submit answers", onClick = { submitAnswers() }, modifier = Modifier.fillMaxWidth())
        } else if (submitStep == 1) {
            AppButton(text = "Submit corrections", onClick = { submitCorrections() }, modifier = Modifier.fillMaxWidth())
        } else {
            Button(
                onClick = { },
                enabled = false,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFF2E7D32),
                    disabledContentColor = Color.White
                )
            ) { Text("Submitted") }
        }

        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            text = "Back",
            onClick = { stopAudio(); onBack() },
            modifier = Modifier.fillMaxWidth(),
            backgroundResId = R.drawable.redbutton
        )
    }
}

@Composable
fun SentenceAnswerRow(
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
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            modifier = Modifier
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
                    if (submitStep == 2 || currentCorrect) {
                        Text("✓", color = Color(0xFF2E7D32), fontSize = 72.sp)
                    } else {
                        Text("✕", color = Color(0xFFC62828), fontSize = 72.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                AppButton(text = "▶", onClick = onPlay, modifier = Modifier.width(76.dp), fontSize = 20)
                Spacer(modifier = Modifier.width(8.dp))
                AppButton(text = "Stop", onClick = onStop, modifier = Modifier.width(100.dp), backgroundResId = R.drawable.redbutton, fontSize = 16)
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = answer,
                onValueChange = onAnswerChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text("Plays: $replayCount")

            if (submitStep >= 1) {
                Text(if (firstAttemptCorrect) "First attempt: correct" else "First attempt: incorrect")

                if (cleanAnswer(answer) == "") {
                    Text("Type an answer before using hints.")
                } else if (replayCount < 5) {
                    Text("Hint locked: listen ${5 - replayCount} more time(s).")
                } else if (submitStep < 2 && !currentCorrect) {
                    AppButton(
                        text = "Reveal next word",
                        onClick = onHint,
                        modifier = Modifier.fillMaxWidth(),
                        backgroundResId = R.drawable.graybutton,
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
