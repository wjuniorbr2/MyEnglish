package com.example.myenglish

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myenglish.ui.theme.MyEnglishTheme
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
        HomeworkSentence("Sentence 1", "I like.", 400, 3800),
        HomeworkSentence("Sentence 2", "You eat meat.", 3900, 7600),
        HomeworkSentence("Sentence 3", "I don't like.", 7700, 11500),
        HomeworkSentence("Sentence 4", "They don't drink water.", 11600, 15300),
        HomeworkSentence("Sentence 5", "Do you drink milk?", 15400, 19200),
        HomeworkSentence("Sentence 6", "Do we eat meat?", 19300, 23000),
        HomeworkSentence("Sentence 7", "I like bread with butter.", 23100, 27000),
        HomeworkSentence("Sentence 8", "I eat.", 27100, 31100),
        HomeworkSentence("Sentence 9", "You like.", 31200, 35000),
        HomeworkSentence("Sentence 10", "I don't like.", 35100, 38900),
        HomeworkSentence("Sentence 11", "You don't like.", 39000, 42700),
        HomeworkSentence("Sentence 12", "Do you like?", 42800, 46700),
        HomeworkSentence("Sentence 13", "Do they like?", 46800, 50500),
        HomeworkSentence("Sentence 14", "Don't you like?", 50600, 54400),
        HomeworkSentence("Sentence 15", "Don't they like?", 54500, 58200),
        HomeworkSentence("Sentence 16", "I like to eat.", 58300, 62300),
        HomeworkSentence("Sentence 17", "I eat in the morning.", 62400, 67200),
        HomeworkSentence("Sentence 18", "I drink milk in the morning.", 67300, 73000),
        HomeworkSentence("Sentence 19", "Do you like milk?", 73100, 77800),
        HomeworkSentence("Sentence 20", "They don't eat bread with butter.", 77900, 84400),
        HomeworkSentence("Sentence 21", "I don't like to eat in the morning.", 84500, 91200),
        HomeworkSentence("Sentence 22", "I drink water.", 91300, 95500),
        HomeworkSentence("Sentence 23", "I eat bread.", 95600, 99600),
        HomeworkSentence("Sentence 24", "I drink juice.", 99700, 103600),
        HomeworkSentence("Sentence 25", "I don't like milk.", 103700, 107600),
        HomeworkSentence("Sentence 26", "I don't drink in the morning.", 107700, 112500),
        HomeworkSentence("Sentence 27", "You eat.", 112600, 115800),
        HomeworkSentence("Sentence 28", "We like.", 115900, 119000),
        HomeworkSentence("Sentence 29", "They like to drink at night.", 119100, 123000),
        HomeworkSentence("Sentence 30", "We don't drink in the morning.", 123100, 127500),
        HomeworkSentence("Sentence 31", "Do you like butter?", 127600, 131500),
        HomeworkSentence("Sentence 32", "Do they like to drink beer?", 131600, 136000)
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
    submitted: Boolean,
    hintCount: Int,
    correctText: String
): Boolean {
    return submitted &&
        cleanAnswer(answer) != "" &&
        replayCount >= 5 &&
        hintCount < countWords(correctText)
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

    val independentCorrect = remember {
        val list = mutableStateListOf<Boolean>()
        var index = 0
        while (index < lessonSentences.size) {
            list.add(false)
            index = index + 1
        }
        list
    }

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
        var index = 0

        while (index < lessonSentences.size) {
            val correct = isCorrectAnswer(answers[index], lessonSentences[index].correctText)
            independentCorrect[index] = correct

            if (correct) {
                score = score + 1
            }

            index = index + 1
        }

        independentScore = score
        submitted = true
        submittedMessage = "Submitted! Independent score: $score / ${lessonSentences.size}"
    }

    fun buildTeacherReport(): String {
        val builder = StringBuilder()
        var index = 0

        builder.append("Original score: ")
        builder.append(independentScore)
        builder.append(" / ")
        builder.append(lessonSentences.size)
        builder.append("\n")

        while (index < lessonSentences.size) {
            builder.append(lessonSentences[index].label)
            builder.append(": plays = ")
            builder.append(replayCounts[index])
            builder.append(", hints = ")
            builder.append(hintCounts[index])
            builder.append(" word(s)\n")

            index = index + 1
        }

        return builder.toString()
    }

    DisposableEffect(Unit) {
        onDispose {
            stopAudio()
        }
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        Text("$lessonName - Homework", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { playAudioPart(0, 136000, "full audio test") }) {
            Text("Test full audio")
        }

        if (playbackMessage != "") {
            Spacer(modifier = Modifier.height(8.dp))
            Text(playbackMessage)
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
                submitted = submitted,
                independentCorrect = independentCorrect[currentIndex],
                hintCount = hintCounts[currentIndex],
                onPlay = {
                    replayCounts[currentIndex] = replayCounts[currentIndex] + 1
                    playAudioPart(
                        currentSentence.startMs,
                        currentSentence.endMs,
                        currentSentence.label
                    )
                },
                onStop = { stopAudio() },
                onHint = {
                    if (canUseHint(
                            answers[currentIndex],
                            replayCounts[currentIndex],
                            submitted,
                            hintCounts[currentIndex],
                            currentSentence.correctText
                        )
                    ) {
                        hintCounts[currentIndex] = hintCounts[currentIndex] + 1
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            rowIndex = rowIndex + 1
        }

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
            Text(buildTeacherReport())
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
    sentence: HomeworkSentence,
    answer: String,
    onAnswerChange: (String) -> Unit,
    replayCount: Int,
    submitted: Boolean,
    independentCorrect: Boolean,
    hintCount: Int,
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

        Text(sentence.label)
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

        if (cleanAnswer(answer) == "") {
            Text("Type an answer before using hints.")
        } else if (replayCount < 5) {
            Text("Hint locked: listen ${5 - replayCount} more time(s).")
        } else {
            Button(onClick = onHint) {
                Text("Reveal next word")
            }
        }

        if (hintCount > 0) {
            Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}")
            Text("Words revealed for this sentence: $hintCount")
        }
    }
}
