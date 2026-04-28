package com.example.myenglish

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myenglish.ui.theme.MyEnglishTheme

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
        HomeworkSentence("Sentence 1", "I like.", 250, 1200),
        HomeworkSentence("Sentence 2", "You eat meat.", 1650, 3200),
        HomeworkSentence("Sentence 3", "I don't like.", 3650, 5200),
        HomeworkSentence("Sentence 4", "They don't drink water.", 5650, 7800),
        HomeworkSentence("Sentence 5", "Do you drink milk?", 8300, 10000),
        HomeworkSentence("Sentence 6", "Do we eat meat?", 10450, 12200),
        HomeworkSentence("Sentence 7", "I like bread with butter.", 12750, 15200),
        HomeworkSentence("Sentence 8", "I eat.", 15800, 16800),
        HomeworkSentence("Sentence 9", "You like.", 17500, 18500),
        HomeworkSentence("Sentence 10", "I don't like.", 19200, 20500),
        HomeworkSentence("Sentence 11", "You don't like.", 21000, 22400),
        HomeworkSentence("Sentence 12", "Do you like?", 22900, 24000),
        HomeworkSentence("Sentence 13", "Do they like?", 24650, 25800),
        HomeworkSentence("Sentence 14", "Don't you like?", 26500, 27800),
        HomeworkSentence("Sentence 15", "Don't they like?", 28500, 29900),
        HomeworkSentence("Sentence 16", "I like to eat.", 30500, 32200),
        HomeworkSentence("Sentence 17", "I eat in the morning.", 32700, 34700),
        HomeworkSentence("Sentence 18", "I drink milk in the morning.", 35200, 37500),
        HomeworkSentence("Sentence 19", "Do you like milk?", 38200, 39800),
        HomeworkSentence("Sentence 20", "They don't eat bread with butter.", 40300, 43000),
        HomeworkSentence("Sentence 21", "I don't like to eat in the morning.", 43550, 46400),
        HomeworkSentence("Sentence 22", "I drink water.", 47000, 48600),
        HomeworkSentence("Sentence 23", "I eat bread.", 49300, 50800),
        HomeworkSentence("Sentence 24", "I drink juice.", 51500, 53000),
        HomeworkSentence("Sentence 25", "I don't like milk.", 53750, 55500),
        HomeworkSentence("Sentence 26", "I don't drink in the morning.", 56100, 58500),
        HomeworkSentence("Sentence 27", "You eat.", 59100, 60300),
        HomeworkSentence("Sentence 28", "We like.", 60950, 62200),
        HomeworkSentence("Sentence 29", "They like to drink at night.", 62800, 65200),
        HomeworkSentence("Sentence 30", "We don't drink in the morning.", 65850, 68400),
        HomeworkSentence("Sentence 31", "Do you like butter?", 69050, 70600),
        HomeworkSentence("Sentence 32", "Do they like to drink beer?", 71100, 73400)
    )
}

fun normalizeAnswer(text: String): String {
    return text
        .trim()
        .lowercase()
        .replace("'", "")
        .replace(Regex("[^a-z0-9 ]"), "")
        .replace(Regex("\\s+"), " ")
}

fun isCorrectAnswer(studentAnswer: String, correctAnswer: String): Boolean {
    return normalizeAnswer(studentAnswer) == normalizeAnswer(correctAnswer)
}

fun countWords(text: String): Int {
    val cleanText = text.trim().trimEnd('.', '?', '!')
    if (cleanText == "") return 0
    return cleanText.split(" ").size
}

fun revealedHintText(correctText: String, hintCount: Int): String {
    val cleanText = correctText.trim().trimEnd('.', '?', '!')
    if (cleanText == "" || hintCount <= 0) return ""

    val words = cleanText.split(" ")
    val safeHintCount = hintCount.coerceAtMost(words.size)
    return words.take(safeHintCount).joinToString(" ")
}

@Composable
fun LessonListScreen() {
    var currentScreen by remember { mutableStateOf("home") }
    var selectedLesson by remember { mutableStateOf<String?>(null) }

    when (currentScreen) {
        "home" -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text("MyEnglish", style = MaterialTheme.typography.headlineMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    selectedLesson = "Lesson 1"
                    currentScreen = "lesson"
                }) { Text("Lesson 1") }

                Button(onClick = {
                    selectedLesson = "Lesson 2"
                    currentScreen = "lesson"
                }) { Text("Lesson 2") }

                Button(onClick = {
                    selectedLesson = "Lesson 3"
                    currentScreen = "lesson"
                }) { Text("Lesson 3") }
            }
        }

        "lesson" -> {
            LessonScreen(
                lessonName = selectedLesson ?: "Lesson",
                onBack = { currentScreen = "home" },
                onOpenHomework = { currentScreen = "homework" }
            )
        }

        "homework" -> {
            HomeworkScreen(
                lessonName = selectedLesson ?: "Lesson",
                onBack = { currentScreen = "lesson" }
            )
        }
    }
}

@Composable
fun LessonScreen(
    lessonName: String,
    onBack: () -> Unit,
    onOpenHomework: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(lessonName, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onOpenHomework) { Text("Homework") }
        Button(onClick = { }) { Text("Vocabulary") }
        Button(onClick = { }) { Text("Practice") }
        Button(onClick = { }) { Text("Grammar") }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) { Text("Back") }
    }
}

@Composable
fun HomeworkScreen(lessonName: String, onBack: () -> Unit) {
    val lessonSentences = Lesson1Homework1Audio.sentences
    val answers = remember { mutableStateListOf("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "") }
    val replayCounts = remember { mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0) }
    val hintCounts = remember { mutableStateListOf(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0) }
    val independentCorrect = remember { mutableStateListOf(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false) }

    var submitted by remember { mutableStateOf(false) }
    var independentScore by remember { mutableIntStateOf(0) }
    var submittedMessage by remember { mutableStateOf("") }
    var playbackMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val handler = remember { Handler(Looper.getMainLooper()) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

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
        playbackMessage = ""
    }

    fun playAudioPart(startMs: Int, endMs: Int, label: String) {
        stopAudio()

        val player = MediaPlayer.create(context, Lesson1Homework1Audio.AUDIO_RES_ID)
        if (player == null) {
            playbackMessage = "Audio file not found."
            return
        }

        mediaPlayer = player
        playbackMessage = "Playing $label"

        player.setVolume(1.0f, 1.0f)
        player.seekTo(startMs)
        player.start()

        handler.postDelayed({
            if (mediaPlayer == player) {
                stopAudio()
            }
        }, (endMs - startMs).toLong())
    }

    fun submitHomework() {
        stopAudio()

        var score = 0

        lessonSentences.forEachIndexed { index, sentence ->
            val correct = isCorrectAnswer(answers[index], sentence.correctText)
            independentCorrect[index] = correct
            if (correct) {
                score = score + 1
            }
        }

        independentScore = score
        submitted = true
        submittedMessage = "Submitted! Independent score: $score / ${lessonSentences.size}"
    }

    DisposableEffect(Unit) {
        onDispose {
            stopAudio()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Text("$lessonName - Homework", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { playAudioPart(0, 125000, "full audio test") }) {
                Text("Test full audio")
            }

            if (playbackMessage != "") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(playbackMessage)
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        itemsIndexed(lessonSentences) { index, sentence ->
            SentenceAnswerRow(
                sentenceLabel = sentence.label,
                answer = answers[index],
                onAnswerChange = { answers[index] = it },
                replayCount = replayCounts[index],
                submitted = submitted,
                independentCorrect = independentCorrect[index],
                hintCount = hintCounts[index],
                correctText = sentence.correctText,
                onPlay = {
                    replayCounts[index] = replayCounts[index] + 1
                    playAudioPart(sentence.startMs, sentence.endMs, sentence.label)
                },
                onStop = { stopAudio() },
                onHint = {
                    if (submitted && answers[index].trim() != "" && replayCounts[index] >= 5 && hintCounts[index] < countWords(sentence.correctText)) {
                        hintCounts[index] = hintCounts[index] + 1
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            Button(onClick = { submitHomework() }) {
                Text("Submit")
            }

            if (submittedMessage != "") {
                Spacer(modifier = Modifier.height(8.dp))
                Text(submittedMessage)
            }

            if (submitted) {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Teacher report", style = MaterialTheme.typography.titleMedium)
                Text("Original score: $independentScore / ${lessonSentences.size}")
                Text("Hints are tracked per sentence as words revealed.")

                Spacer(modifier = Modifier.height(8.dp))

                lessonSentences.forEachIndexed { index, sentence ->
                    Text("${sentence.label}: plays = ${replayCounts[index]}, hints = ${hintCounts[index]} word(s)")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                stopAudio()
                onBack()
            }) {
                Text("Back")
            }
        }
    }
}

@Composable
fun SentenceAnswerRow(
    sentenceLabel: String,
    answer: String,
    onAnswerChange: (String) -> Unit,
    replayCount: Int,
    submitted: Boolean,
    independentCorrect: Boolean,
    hintCount: Int,
    correctText: String,
    onPlay: () -> Unit,
    onStop: () -> Unit,
    onHint: () -> Unit
) {
    Row {
        Button(onClick = onPlay) {
            Text("▶")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Button(onClick = onStop) {
            Text("Stop")
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(sentenceLabel)
    }

    Spacer(modifier = Modifier.height(8.dp))

    TextField(
        value = answer,
        onValueChange = onAnswerChange,
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(4.dp))
    Text("Plays: $replayCount")

    if (submitted) {
        if (independentCorrect) {
            Text("Independent attempt: correct")
        } else {
            Text("Independent attempt: incorrect")
        }

        if (answer.trim() == "") {
            Text("Type an answer before using hints.")
        } else if (replayCount < 5) {
            Text("Hint locked: listen ${5 - replayCount} more time(s).")
        } else {
            Button(onClick = onHint) {
                Text("Reveal next word")
            }
        }

        if (hintCount > 0) {
            Text("Hint: ${revealedHintText(correctText, hintCount)}")
            Text("Words revealed for this sentence: $hintCount")
        }
    }
}
