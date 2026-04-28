package com.example.myenglish

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.ui.theme.MyEnglishTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.StringTokenizer

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyEnglishTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    LessonListScreen()
                }
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
    if (cleanText == "") {
        return 0
    }

    val tokenizer = StringTokenizer(cleanText, " ")
    return tokenizer.countTokens()
}

fun revealedHintText(correctText: String, hintCount: Int): String {
    val cleanText = cleanAnswer(correctText)
    if (cleanText == "" || hintCount <= 0) {
        return ""
    }

    val tokenizer = StringTokenizer(cleanText, " ")
    val builder = StringBuilder()
    var wordsAdded = 0

    while (tokenizer.hasMoreTokens() && wordsAdded < hintCount) {
        if (builder.length > 0) {
            builder.append(" ")
        }

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

@Composable
fun LessonListScreen() {
    val lessonSentences = Lesson1Homework1Audio.sentences

    var currentScreen by remember { mutableStateOf("home") }
    var selectedLesson by remember { mutableStateOf<String?>(null) }
    var showHomeworkChoices by remember { mutableStateOf(false) }

    val answers = remember {
        val list = mutableStateListOf<String>()
        var index = 0
        while (index < lessonSentences.size) {
            list.add("")
            index = index + 1
        }
        list
    }

    val replayCounts = remember {
        val list = mutableStateListOf<Int>()
        var index = 0
        while (index < lessonSentences.size) {
            list.add(0)
            index = index + 1
        }
        list
    }

    val hintCounts = remember {
        val list = mutableStateListOf<Int>()
        var index = 0
        while (index < lessonSentences.size) {
            list.add(0)
            index = index + 1
        }
        list
    }

    val firstAttemptCorrect = remember {
        val list = mutableStateListOf<Boolean>()
        var index = 0
        while (index < lessonSentences.size) {
            list.add(false)
            index = index + 1
        }
        list
    }

    var submitStep by remember { mutableIntStateOf(0) }
    var firstAttemptScore by remember { mutableIntStateOf(0) }
    var submittedMessage by remember { mutableStateOf("") }
    var teacherReportToSend by remember { mutableStateOf("") }

    BackHandler(enabled = currentScreen != "home") {
        if (currentScreen == "homework") {
            currentScreen = "lesson"
        } else if (currentScreen == "lesson") {
            currentScreen = "home"
        }
    }

    when (currentScreen) {
        "home" -> {
            HomeScreen(
                onOpenLesson = { lessonName ->
                    selectedLesson = lessonName
                    showHomeworkChoices = false
                    currentScreen = "lesson"
                }
            )
        }

        "lesson" -> {
            LessonScreen(
                lessonName = selectedLesson ?: "Lesson",
                showHomeworkChoices = showHomeworkChoices,
                onShowHomeworkChoices = { showHomeworkChoices = true },
                onOpenListeningHomework = { currentScreen = "homework" },
                onBack = { currentScreen = "home" }
            )
        }

        "homework" -> {
            HomeworkScreen(
                lessonName = selectedLesson ?: "Lesson",
                lessonSentences = lessonSentences,
                answers = answers,
                replayCounts = replayCounts,
                hintCounts = hintCounts,
                firstAttemptCorrect = firstAttemptCorrect,
                submitStep = submitStep,
                firstAttemptScore = firstAttemptScore,
                submittedMessage = submittedMessage,
                teacherReportToSend = teacherReportToSend,
                onFirstAttemptScoreChange = { firstAttemptScore = it },
                onSubmitStepChange = { submitStep = it },
                onSubmittedMessageChange = { submittedMessage = it },
                onTeacherReportChange = { teacherReportToSend = it },
                onBack = { currentScreen = "lesson" }
            )
        }
    }
}

@Composable
fun HomeScreen(onOpenLesson: (String) -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("My English", style = MaterialTheme.typography.headlineMedium)
        Text("Book 1", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        var lessonNumber = 1
        while (lessonNumber <= 31) {
            Button(
                onClick = { onOpenLesson("Lesson $lessonNumber") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lesson $lessonNumber")
            }
            Spacer(modifier = Modifier.height(8.dp))
            lessonNumber = lessonNumber + 1
        }
    }
}

@Composable
fun LessonScreen(
    lessonName: String,
    showHomeworkChoices: Boolean,
    onShowHomeworkChoices: () -> Unit,
    onOpenListeningHomework: () -> Unit,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(lessonName, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Vocabulary") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Grammar") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { }, modifier = Modifier.fillMaxWidth()) { Text("Practice") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onShowHomeworkChoices, modifier = Modifier.fillMaxWidth()) { Text("Homework") }

        if (showHomeworkChoices) {
            Spacer(modifier = Modifier.height(16.dp))
            Text("Choose homework", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onOpenListeningHomework, modifier = Modifier.fillMaxWidth()) {
                Text("Listening homework")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(), enabled = false) {
                Text("Written homework")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { }, modifier = Modifier.fillMaxWidth(), enabled = false) {
                Text("Spoken homework")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun HomeworkScreen(
    lessonName: String,
    lessonSentences: Array<HomeworkSentence>,
    answers: MutableList<String>,
    replayCounts: MutableList<Int>,
    hintCounts: MutableList<Int>,
    firstAttemptCorrect: MutableList<Boolean>,
    submitStep: Int,
    firstAttemptScore: Int,
    submittedMessage: String,
    teacherReportToSend: String,
    onFirstAttemptScoreChange: (Int) -> Unit,
    onSubmitStepChange: (Int) -> Unit,
    onSubmittedMessageChange: (String) -> Unit,
    onTeacherReportChange: (String) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val handler = remember { Handler(Looper.getMainLooper()) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    LaunchedEffect(scrollState.isScrollInProgress) {
        if (scrollState.isScrollInProgress) {
            focusManager.clearFocus()
        }
    }

    fun stopAudio() {
        handler.removeCallbacksAndMessages(null)
        try {
            mediaPlayer?.stop()
        } catch (_: Exception) {
            // Already stopped or not prepared.
        }
        try {
            mediaPlayer?.release()
        } catch (_: Exception) {
            // Already released.
        }
        mediaPlayer = null
    }

    fun playAudioPart(startMs: Int, endMs: Int, label: String) {
        stopAudio()

        val player = MediaPlayer.create(context, Lesson1Homework1Audio.AUDIO_RES_ID)
        if (player == null) {
            return
        }

        mediaPlayer = player
        player.setVolume(1.0f, 1.0f)
        player.seekTo(startMs)
        player.start()

        handler.postDelayed({
            if (mediaPlayer == player) {
                stopAudio()
            }
        }, (endMs - startMs).toLong())
    }

    fun buildTeacherReport(score: Int, correctionsSubmitted: Boolean): String {
        val builder = StringBuilder()
        var index = 0

        builder.append("Submitted at: ")
        builder.append(currentDateTimeText())
        builder.append("\n")
        builder.append("Original score: ")
        builder.append(score)
        builder.append(" / ")
        builder.append(lessonSentences.size)
        builder.append("\n")
        builder.append("Student submitted answers: yes\n")
        builder.append("Student submitted corrections: ")
        if (correctionsSubmitted) {
            builder.append("yes\n")
        } else {
            builder.append("no\n")
        }

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
            if (correct) {
                score = score + 1
            }
            index = index + 1
        }

        onFirstAttemptScoreChange(score)
        onSubmitStepChange(1)
        onSubmittedMessageChange("First attempt score: $score / ${lessonSentences.size}")
        onTeacherReportChange(buildTeacherReport(score, false))
        coroutineScope.launch { scrollState.scrollTo(0) }
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
        } else {
            onSubmittedMessageChange("Some corrections are still incorrect. Please check the red X sentences.")
            onTeacherReportChange(buildTeacherReport(firstAttemptScore, false))
        }
        coroutineScope.launch { scrollState.scrollTo(0) }
    }

    DisposableEffect(Unit) {
        onDispose {
            stopAudio()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("$lessonName - Listening homework", style = MaterialTheme.typography.headlineMedium)

        if (submittedMessage != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(submittedMessage)
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
                onPlay = {
                    replayCounts[currentIndex] = replayCounts[currentIndex] + 1
                    playAudioPart(currentSentence.startMs, currentSentence.endMs, currentSentence.label)
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
            Button(onClick = { submitAnswers() }) { Text("Submit answers") }
        } else if (submitStep == 1) {
            Button(onClick = { submitCorrections() }) { Text("Submit corrections") }
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

        Button(onClick = {
            stopAudio()
            onBack()
        }) { Text("Back") }
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
                Button(onClick = onPlay) { Text("▶") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onStop) { Text("Stop") }
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = answer,
                onValueChange = onAnswerChange,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text("Plays: $replayCount")

            if (submitStep >= 1) {
                if (firstAttemptCorrect) {
                    Text("First attempt: correct")
                } else {
                    Text("First attempt: incorrect")
                }

                if (cleanAnswer(answer) == "") {
                    Text("Type an answer before using hints.")
                } else if (replayCount < 5) {
                    Text("Hint locked: listen ${5 - replayCount} more time(s).")
                } else if (submitStep < 2 && !currentCorrect) {
                    Button(onClick = onHint) { Text("Reveal next word") }
                }

                if (hintCount > 0) {
                    Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}")
                    Text("Hints used: $hintCount")
                }
            }
        }
    }
}
