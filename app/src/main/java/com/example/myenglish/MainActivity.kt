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

    val context = LocalContext.current
    val handler = remember { Handler(Looper.getMainLooper()) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }

    fun playAudioPart(startMs: Int, endMs: Int) {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null

        val player = MediaPlayer.create(context, R.raw.lesson1)
        mediaPlayer = player

        player.seekTo(startMs)
        player.start()

        handler.postDelayed({
            player.stop()
            player.release()
            if (mediaPlayer == player) {
                mediaPlayer = null
            }
        }, (endMs - startMs).toLong())
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("$lessonName - Homework", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        SentenceAnswerRow(
            sentenceLabel = "Sentence 1",
            answer = answers[0],
            onAnswerChange = { answers[0] = it },
            onPlay = { playAudioPart(0, 2000) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SentenceAnswerRow(
            sentenceLabel = "Sentence 2",
            answer = answers[1],
            onAnswerChange = { answers[1] = it },
            onPlay = { playAudioPart(2500, 4500) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        SentenceAnswerRow(
            sentenceLabel = "Sentence 3",
            answer = answers[2],
            onAnswerChange = { answers[2] = it },
            onPlay = { playAudioPart(5000, 7000) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            submittedMessage = "Submitted!"
        }) {
            Text("Submit")
        }

        if (submittedMessage != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(submittedMessage)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}

@Composable
fun SentenceAnswerRow(
    sentenceLabel: String,
    answer: String,
    onAnswerChange: (String) -> Unit,
    onPlay: () -> Unit
) {
    Row {
        Button(onClick = onPlay) {
            Text("▶")
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
