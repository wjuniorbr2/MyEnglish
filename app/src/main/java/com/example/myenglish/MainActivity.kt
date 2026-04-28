package com.example.myenglish

import android.os.Bundle
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

    val sentences = remember {
        mutableStateListOf(
            "I drink milk",
            "I like butter",
            "I eat bread"
        )
    }

    val answers = remember {
        mutableStateListOf("", "", "")
    }

    var submittedMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text("$lessonName - Homework", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        sentences.forEachIndexed { index, sentence ->

            Text("Sentence ${index + 1}")

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = answers[index],
                onValueChange = { answers[index] = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Button(onClick = {
            submittedMessage = "Submitted!"
        }) {
            Text("Submit")
        }

        if (submittedMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(submittedMessage)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onBack) {
            Text("Back")
        }
    }
}