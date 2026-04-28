package com.example.myenglish
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.os.Bundle
import android.util.Pair
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
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
                lessonName = selectedLesson!!,
                onBack = { currentScreen = "home" },
                onOpenHomework = { currentScreen = "homework" }
            )
        }

        "homework" -> {
            HomeworkScreen(
                lessonName = selectedLesson!!,
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

    val answers = remember {
        mutableStateListOf("", "", "")
    }

    var submittedMessage by remember { mutableStateOf("") }

    val context = androidx.compose.ui.platform.LocalContext.current
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    val handler = remember { Handler(Looper.getMainLooper()) }

    val timings = listOf(
        Pair(0, 2000),    // Sentence 1
        Pair(2500, 4500), // Sentence 2
        Pair(5000, 7000)  // Sentence 3
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("$lessonName - Homework", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Button(onClick = {

                val (start, end) = timings[0]

                mediaPlayer?.release()

                mediaPlayer = MediaPlayer.create(context, R.raw.lesson1).apply {
                    seekTo(start)
                    start()

                    handler.postDelayed({
                        stop()
                        release()
                        mediaPlayer = null
                    }, (end - start).toLong())
                }

            }) {
                Text("▶")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text("Sentence 1")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Button(onClick = {

                val (start, end) = timings[1]

                mediaPlayer?.release()

                mediaPlayer = MediaPlayer.create(context, R.raw.lesson1).apply {
                    seekTo(start)
                    start()

                    handler.postDelayed({
                        stop()
                        release()
                        mediaPlayer = null
                    }, (end - start).toLong())
                }

            }) {
                Text("▶")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text("Sentence 1")
        }
        Spacer(modifier = Modifier.height(12.dp))

        Row {
            Button(onClick = {

                val (start, end) = timings[0]

                mediaPlayer?.release()

                mediaPlayer = MediaPlayer.create(context, R.raw.lesson1).apply {
                    seekTo(start)
                    start()

                    handler.postDelayed({
                        stop()
                        release()
                        mediaPlayer = null
                    }, (end - start).toLong())
                }

            }) {
                Text("▶")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text("Sentence 1")
        }
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