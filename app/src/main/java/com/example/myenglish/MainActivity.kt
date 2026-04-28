package com.example.myenglish

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
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

private object Lesson1Homework1Audio {
    val AUDIO_RES_ID = R.raw.lesson1

    const val SENTENCE_1_START_MS = 900
    const val SENTENCE_1_END_MS = 13670

    const val SENTENCE_2_START_MS = 15430
    const val SENTENCE_2_END_MS = 48720

    const val SENTENCE_3_START_MS = 50090
    const val SENTENCE_3_END_MS = 96740
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
    val answers = remember { mutableStateListOf("", "", "") }
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

    DisposableEffect(Unit) {
        onDispose {
            stopAudio()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
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

        SentenceAnswerRow(
            sentenceLabel = "Sentence 1",
            answer = answers[0],
            onAnswerChange = { answers[0] = it },
            onPlay = {
                playAudioPart(
                    Lesson1Homework1Audio.SENTENCE_1_START_MS,
                    Lesson1Homework1Audio.SENTENCE_1_END_MS,
                    "Sentence 1"
                )
            },
            onStop = { stopAudio() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SentenceAnswerRow(
            sentenceLabel = "Sentence 2",
            answer = answers[1],
            onAnswerChange = { answers[1] = it },
            onPlay = {
                playAudioPart(
                    Lesson1Homework1Audio.SENTENCE_2_START_MS,
                    Lesson1Homework1Audio.SENTENCE_2_END_MS,
                    "Sentence 2"
                )
            },
            onStop = { stopAudio() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SentenceAnswerRow(
            sentenceLabel = "Sentence 3",
            answer = answers[2],
            onAnswerChange = { answers[2] = it },
            onPlay = {
                playAudioPart(
                    Lesson1Homework1Audio.SENTENCE_3_START_MS,
                    Lesson1Homework1Audio.SENTENCE_3_END_MS,
                    "Sentence 3"
                )
            },
            onStop = { stopAudio() }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            stopAudio()
            submittedMessage = "Submitted!"
        }) {
            Text("Submit")
        }

        if (submittedMessage != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(submittedMessage)
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

@Composable
fun SentenceAnswerRow(
    sentenceLabel: String,
    answer: String,
    onAnswerChange: (String) -> Unit,
    onPlay: () -> Unit,
    onStop: () -> Unit
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
}
