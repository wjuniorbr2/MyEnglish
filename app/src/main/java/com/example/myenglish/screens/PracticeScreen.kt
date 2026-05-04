package com.example.myenglish.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.myenglish.R
import com.example.myenglish.components.ArtButton
import com.example.myenglish.components.Header
import com.example.myenglish.data.SpokenHomeworkSentence
import com.example.myenglish.sendHomeworkReportToTeacher
import com.example.myenglish.utils.cleanAnswer
import com.example.myenglish.utils.currentDateTimeText
import com.example.myenglish.utils.isCorrectAnswer
import java.util.Locale

private val practiceRed = Color(0xFFC62828)
private val practiceGreen = Color(0xFF2E7D32)
private val practiceBlue = Color(0xFF0D3D7A)

private data class PracticeResult(
    val number: Int,
    val portuguese: String,
    val expected: String,
    val firstAnswer: String,
    val finalAnswer: String,
    val attempts: Int,
    val hintsUsed: Int,
    val firstCorrect: Boolean,
    val finalCorrect: Boolean
)

@Composable
fun PracticeScreen(
    name: String,
    studentName: String,
    sentences: Array<SpokenHomeworkSentence>,
    back: () -> Unit
) {
    val context = LocalContext.current
    val scroll = rememberScrollState()
    val remainingIndexes = remember(sentences.size) { mutableStateListOf<Int>().apply { addAll(sentences.indices.shuffled()) } }
    val results = remember { mutableStateListOf<PracticeResult>() }

    var currentIndex by remember { mutableIntStateOf(if (sentences.isNotEmpty()) remainingIndexes.removeAt(0) else -1) }
    var answer by remember { mutableStateOf("") }
    var firstAnswer by remember { mutableStateOf("") }
    var attempts by remember { mutableIntStateOf(0) }
    var hintState by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    var msgError by remember { mutableStateOf(false) }
    var activeListening by remember { mutableStateOf(false) }
    var sending by remember { mutableStateOf(false) }
    var startedAtMillis by remember { mutableStateOf(System.currentTimeMillis()) }
    var startedAtText by remember { mutableStateOf(currentDateTimeText()) }
    var ttsReady by remember { mutableStateOf(false) }

    val textToSpeech = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) ttsReady = true
        }
    }

    LaunchedEffect(ttsReady) {
        if (ttsReady) {
            val result = textToSpeech.setLanguage(Locale.US)
            ttsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
            textToSpeech.setSpeechRate(0.85f)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    fun setTop(text: String, error: Boolean) {
        msg = text
        msgError = error
    }

    fun currentSentence(): SpokenHomeworkSentence? {
        return if (currentIndex in sentences.indices) sentences[currentIndex] else null
    }

    fun playAnswer() {
        val sentence = currentSentence() ?: return
        if (ttsReady) {
            textToSpeech.speak(sentence.english, TextToSpeech.QUEUE_FLUSH, null, "practice_answer_${sentence.english}")
        } else {
            setTop("This phone is not ready to pronounce English yet.", true)
        }
    }

    fun recordCurrentIfNeeded() {
        val sentence = currentSentence() ?: return
        if (answer.isBlank() && attempts == 0 && hintState.isBlank()) return

        val first = if (firstAnswer.isNotBlank()) firstAnswer else answer
        results.add(
            PracticeResult(
                number = results.size + 1,
                portuguese = sentence.portuguese,
                expected = sentence.english,
                firstAnswer = first,
                finalAnswer = answer,
                attempts = attempts,
                hintsUsed = selectedHintIndexes(hintState).size,
                firstCorrect = isCorrectAnswer(first, sentence.english),
                finalCorrect = isCorrectAnswer(answer, sentence.english)
            )
        )
    }

    fun resetCurrentState() {
        answer = ""
        firstAnswer = ""
        attempts = 0
        hintState = ""
    }

    fun nextSentence() {
        recordCurrentIfNeeded()
        if (remainingIndexes.isEmpty()) {
            remainingIndexes.addAll(sentences.indices.shuffled())
        }
        currentIndex = if (remainingIndexes.isNotEmpty()) remainingIndexes.removeAt(0) else -1
        resetCurrentState()
        setTop("Next sentence loaded. The microphone is suspiciously ready.", false)
    }

    fun formatDuration(ms: Long): String {
        val totalSeconds = (ms / 1000).coerceAtLeast(0)
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "${minutes}m ${seconds}s"
    }

    fun buildReport(): String {
        val finishedAtMillis = System.currentTimeMillis()
        val duration = formatDuration(finishedAtMillis - startedAtMillis)
        val finishedAtText = currentDateTimeText()
        val firstCorrect = results.count { it.firstCorrect }
        val finalCorrect = results.count { it.finalCorrect }
        val totalHints = results.sumOf { it.hintsUsed }
        val totalAttempts = results.sumOf { it.attempts }

        val builder = StringBuilder()
        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Started at: ").append(startedAtText).append("\n")
        builder.append("Finished at: ").append(finishedAtText).append("\n")
        builder.append("Practice duration: ").append(duration).append("\n")
        builder.append("Lesson: ").append(name).append("\n")
        builder.append("Practice: Spoken practice\n")
        builder.append("Phrases practiced: ").append(results.size).append("\n")
        builder.append("First try correct: ").append(firstCorrect).append(" / ").append(results.size).append("\n")
        builder.append("Final correct: ").append(finalCorrect).append(" / ").append(results.size).append("\n")
        builder.append("Total attempts: ").append(totalAttempts).append("\n")
        builder.append("Hints used: ").append(totalHints).append("\n\n")

        results.forEach { item ->
            builder.append(item.number).append(". Portuguese: ").append(item.portuguese).append("\n")
            builder.append("Expected English: ").append(item.expected).append("\n")
            builder.append("First recognized speech: ").append(item.firstAnswer).append("\n")
            builder.append("Final recognized speech: ").append(item.finalAnswer).append("\n")
            builder.append("Attempts: ").append(item.attempts).append("\n")
            builder.append("Hints used: ").append(item.hintsUsed).append("\n")
            builder.append("First try correct: ").append(if (item.firstCorrect) "yes" else "no").append("\n")
            builder.append("Final correct: ").append(if (item.finalCorrect) "yes" else "no").append("\n\n")
        }

        return builder.toString()
    }

    fun finishPractice() {
        if (sending) return
        recordCurrentIfNeeded()
        if (results.isEmpty()) {
            setTop("Practice at least one sentence before sending the report.", true)
            return
        }

        sending = true
        val finalCorrect = results.count { it.finalCorrect }
        val report = buildReport()
        setTop("Sending your practice adventure...", false)

        sendHomeworkReportToTeacher(
            studentName = studentName,
            lessonName = name,
            homeworkType = "Spoken practice",
            scoreText = "$finalCorrect / ${results.size}",
            report = report
        ) { success ->
            Handler(Looper.getMainLooper()).post {
                sending = false
                if (success) {
                    setTop("Practice report sent. The teacher has received your microphone saga.", false)
                    back()
                } else {
                    setTop("Failed to send practice report. Try again when the internet goblin behaves.", true)
                }
            }
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        activeListening = false
        val sentence = currentSentence() ?: return@rememberLauncherForActivityResult
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()?.trim() ?: ""
            if (spokenText.isNotBlank()) {
                attempts++
                val formatted = applyPracticePunctuation(spokenText, sentence.english)
                answer = formatted
                if (firstAnswer.isBlank()) firstAnswer = formatted
                setTop(if (isCorrectAnswer(answer, sentence.english)) "Correct. The sentence behaved." else "Not yet. The red words are doing cartwheels.", !isCorrectAnswer(answer, sentence.english))
            } else {
                setTop("The microphone heard mysterious silence. Try again.", true)
            }
        } else {
            setTop("The robot ear gave up too early. Try again.", true)
        }
    }

    fun launchSpeech() {
        activeListening = true
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak in English")
        }
        speechLauncher.launch(intent)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) launchSpeech() else setTop("Microphone permission is needed for spoken practice.", true)
    }

    fun startSpeaking() {
        val hasPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
        if (hasPermission) launchSpeech() else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    LaunchedEffect(Unit) {
        startedAtMillis = System.currentTimeMillis()
        startedAtText = currentDateTimeText()
    }

    Column(
        Modifier.fillMaxSize().verticalScroll(scroll).padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name, "Practice")

        if (msg.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = if (msgError) practiceRed else practiceGreen,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(shadow = Shadow(Color.White, Offset(1f, 1f), 3f))
            )
        }

        Spacer(Modifier.height(14.dp))

        currentSentence()?.let { sentence ->
            PracticeCard(
                practicedCount = results.size + 1,
                sentence = sentence,
                answer = answer,
                attempts = attempts,
                hintState = hintState,
                speak = { startSpeaking() },
                playAnswer = { playAnswer() },
                openHints = { if (hintState.isBlank()) hintState = "open:" },
                chooseHintWord = { index -> hintState = addHintIndex(hintState, index) }
            )
        }

        Spacer(Modifier.height(14.dp))

        ArtButton(
            text = "Next sentence",
            onClick = { nextSentence() },
            modifier = Modifier.fillMaxWidth(0.85f),
            backgroundResId = R.drawable.graybutton,
            enabled = !sending && !activeListening,
            fontSize = 16
        )

        Spacer(Modifier.height(8.dp))

        ArtButton(
            text = if (sending) "Sending..." else "Finish practice",
            onClick = { finishPractice() },
            modifier = Modifier.fillMaxWidth(0.85f),
            backgroundResId = R.drawable.redbutton,
            enabled = !sending && !activeListening,
            fontSize = 16
        )

        Spacer(Modifier.height(14.dp))

        ArtButton(
            text = if (sending) "Sending..." else "Back",
            onClick = { finishPractice() },
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton,
            enabled = !sending && !activeListening
        )
    }
}

@Composable
private fun PracticeCard(
    practicedCount: Int,
    sentence: SpokenHomeworkSentence,
    answer: String,
    attempts: Int,
    hintState: String,
    speak: () -> Unit,
    playAnswer: () -> Unit,
    openHints: () -> Unit,
    chooseHintWord: (Int) -> Unit
) {
    val ok = isCorrectAnswer(answer, sentence.english)
    val selectedHints = selectedHintIndexes(hintState)
    val tokens = expectedDisplayTokens(sentence.english)

    Card(Modifier.fillMaxWidth(), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$practicedCount. ${sentence.portuguese}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (answer.isNotBlank()) {
                    Text(if (ok) "✓" else "✕", color = if (ok) practiceGreen else practiceRed, fontSize = 54.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                ArtButton("🎙 Speak", speak, Modifier.weight(1f), heightDp = 54, fontSize = 16)
                Spacer(Modifier.width(8.dp))
                ArtButton(
                    text = "",
                    onClick = playAnswer,
                    modifier = Modifier.weight(0.9f),
                    backgroundResId = R.drawable.whitebutton,
                    heightDp = 54,
                    fontSize = 15,
                    content = {
                        Text(
                            text = "🔊 Answer",
                            color = practiceBlue,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            textAlign = TextAlign.Center,
                            style = LocalTextStyle.current.copy(
                                shadow = Shadow(Color.White, Offset(1f, 1f), 2f)
                            )
                        )
                    }
                )
            }

            Spacer(Modifier.height(8.dp))
            Text("Android heard:", fontWeight = FontWeight.Bold, color = Color(0xFF555555))
            if (answer.isNotBlank()) {
                Text(coloredPracticeAnswer(answer, sentence.english), fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
            Text("Tries: $attempts")

            Spacer(Modifier.height(6.dp))
            ArtButton("Choose a spoiler word", openHints, Modifier.fillMaxWidth(0.75f), R.drawable.graybutton, true, 52, 15)

            if (hintState.startsWith("open:")) {
                Spacer(Modifier.height(8.dp))
                Text("Choose a word to reveal.")
                Spacer(Modifier.height(5.dp))
                Row(Modifier.fillMaxWidth()) {
                    for (i in tokens.indices) {
                        val token = tokens[i]
                        val revealed = selectedHints.contains(i)
                        Text(
                            text = if (revealed) token else underlineToken(token),
                            color = if (revealed) Color(0xFF0D3D7A) else Color(0xFF555555),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.padding(end = 8.dp, bottom = 4.dp).clickable { if (!revealed) chooseHintWord(i) }
                        )
                    }
                }
                Text("Spoilers used: ${selectedHints.size}")
            }
        }
    }
}

private fun applyPracticePunctuation(raw: String, expected: String): String {
    val cleaned = raw.trim().removeSuffix(".").removeSuffix("?").removeSuffix("!").trim()
    if (cleaned.isBlank()) return ""
    val firstUpper = cleaned.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    val punctuation = when {
        expected.trim().endsWith("?") -> "?"
        expected.trim().endsWith("!") -> "!"
        else -> "."
    }
    return firstUpper + punctuation
}

private fun coloredPracticeAnswer(answer: String, expected: String) = buildAnnotatedString {
    val expectedWords = cleanAnswer(expected).split(" ").filter { it.isNotBlank() }
    val ranges = wordRanges(answer)
    val studentWords = ranges.map { cleanAnswer(answer.substring(it.first, it.last)) }
    val matchMap = matchingStudentToExpected(studentWords, expectedWords)
    var expectedCursor = 0
    for (i in ranges.indices) {
        val expectedIndex = matchMap[i]
        if (expectedIndex != null && expectedIndex >= expectedCursor) {
            while (expectedCursor < expectedIndex) {
                appendMissingUnderline()
                expectedCursor++
            }
        }
        val start = length
        append(answer.substring(ranges[i].first, ranges[i].last))
        val end = length
        if (studentWords[i].isNotBlank() && expectedIndex == null) {
            addStyle(SpanStyle(color = practiceRed), start, end)
            if (expectedCursor < expectedWords.size) expectedCursor++
        }
        append(" ")
        if (expectedIndex != null && expectedIndex >= expectedCursor) expectedCursor = expectedIndex + 1
    }
    while (expectedCursor < expectedWords.size) {
        appendMissingUnderline()
        expectedCursor++
    }
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.appendMissingUnderline() {
    val start = length
    append("____ ")
    addStyle(SpanStyle(color = practiceRed, textDecoration = TextDecoration.Underline), start, length)
}

private fun matchingStudentToExpected(studentWords: List<String>, expectedWords: List<String>): Map<Int, Int> {
    val result = mutableMapOf<Int, Int>()
    val used = mutableSetOf<Int>()
    for (i in studentWords.indices) {
        var found = -1
        for (j in expectedWords.indices) {
            if (found == -1 && !used.contains(j) && studentWords[i] == expectedWords[j]) found = j
        }
        if (found != -1) {
            result[i] = found
            used.add(found)
        }
    }
    return result
}

private fun selectedHintIndexes(state: String): Set<Int> {
    if (!state.startsWith("open:")) return emptySet()
    val raw = state.removePrefix("open:")
    if (raw.isBlank()) return emptySet()
    return raw.split(",").mapNotNull { it.toIntOrNull() }.toSet()
}

private fun addHintIndex(state: String, index: Int): String {
    val selected = selectedHintIndexes(state).toMutableSet()
    selected.add(index)
    return "open:" + selected.sorted().joinToString(",")
}

private fun expectedDisplayTokens(text: String): List<String> = text.trim().split(" ").filter { it.isNotBlank() }
private fun underlineToken(token: String): String = "____" + token.takeLastWhile { !it.isLetterOrDigit() }

private fun wordRanges(text: String): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    var start = -1
    for (i in text.indices) {
        val word = text[i].isLetterOrDigit() || text[i] == '’' || text[i] == '\''
        if (word && start == -1) start = i
        if (!word && start != -1) {
            ranges.add(IntRange(start, i))
            start = -1
        }
    }
    if (start != -1) ranges.add(IntRange(start, text.length))
    return ranges
}
