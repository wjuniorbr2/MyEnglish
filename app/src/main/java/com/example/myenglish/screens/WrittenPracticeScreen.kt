package com.example.myenglish.screens

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.R
import com.example.myenglish.components.ArtButton
import com.example.myenglish.components.Header
import com.example.myenglish.data.WrittenHomeworkSentence
import com.example.myenglish.sendHomeworkReportToTeacher
import com.example.myenglish.utils.cleanAnswer
import com.example.myenglish.utils.currentDateTimeText
import com.example.myenglish.utils.hideKeyboardOnBackgroundTap
import com.example.myenglish.utils.isCorrectAnswer

private val writtenPracticeRed = Color(0xFFC62828)
private val writtenPracticeGreen = Color(0xFF2E7D32)

private data class WrittenPracticeResult(
    val number: Int,
    val portuguese: String,
    val expected: String,
    val answer: String,
    val hintsUsed: Int,
    val correct: Boolean
)

@Composable
fun WrittenPracticeScreen(
    name: String,
    studentName: String,
    sentences: Array<WrittenHomeworkSentence>,
    back: () -> Unit
) {
    val scroll = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val results = remember { mutableStateListOf<WrittenPracticeResult>() }

    val remainingIndexes = remember(sentences.size) {
        mutableStateListOf<Int>().apply { addAll(sentences.indices.shuffled()) }
    }
    var currentIndex by remember(sentences.size) {
        mutableIntStateOf(if (sentences.isNotEmpty()) remainingIndexes.removeAt(0) else -1)
    }
    var answer by remember { mutableStateOf("") }
    var hintState by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    var msgError by remember { mutableStateOf(false) }
    var sending by remember { mutableStateOf(false) }
    var startedAtText by remember { mutableStateOf(currentDateTimeText()) }
    var startedAtMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    fun currentSentence(): WrittenHomeworkSentence? {
        return if (currentIndex in sentences.indices) sentences[currentIndex] else null
    }

    LaunchedEffect(scroll.isScrollInProgress) {
        if (scroll.isScrollInProgress) {
            focusManager.clearFocus()
            keyboard?.hide()
        }
    }

    fun resetCurrent() {
        answer = ""
        hintState = ""
    }

    fun recordCurrentIfNeeded() {
        val sentence = currentSentence() ?: return
        if (answer.isBlank() && hintState.isBlank()) return

        results.add(
            WrittenPracticeResult(
                number = results.size + 1,
                portuguese = sentence.portuguese,
                expected = sentence.english,
                answer = answer,
                hintsUsed = selectedPracticeHintIndexes(hintState).size,
                correct = isCorrectAnswer(answer, sentence.english)
            )
        )
    }

    fun nextSentence() {
        recordCurrentIfNeeded()
        if (remainingIndexes.isEmpty()) {
            remainingIndexes.addAll(sentences.indices.shuffled())
        }
        currentIndex = if (remainingIndexes.isNotEmpty()) remainingIndexes.removeAt(0) else -1
        resetCurrent()
        msg = "Next sentence loaded."
        msgError = false
    }

    fun buildReport(): String {
        val duration = formatPracticeDuration(System.currentTimeMillis() - startedAtMillis)
        val correct = results.count { it.correct }
        val builder = StringBuilder()
        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Started at: ").append(startedAtText).append("\n")
        builder.append("Finished at: ").append(currentDateTimeText()).append("\n")
        builder.append("Practice duration: ").append(duration).append("\n")
        builder.append("Lesson: ").append(name).append("\n")
        builder.append("Practice: Written practice\n")
        builder.append("Phrases practiced: ").append(results.size).append("\n")
        builder.append("Correct answers: ").append(correct).append(" / ").append(results.size).append("\n\n")

        results.forEach { item ->
            builder.append(item.number).append(". Portuguese: ").append(item.portuguese).append("\n")
            builder.append("Expected English: ").append(item.expected).append("\n")
            builder.append("Written answer: ").append(item.answer).append("\n")
            builder.append("Hints used: ").append(item.hintsUsed).append("\n")
            builder.append("Correct: ").append(if (item.correct) "yes" else "no").append("\n\n")
        }

        return builder.toString()
    }

    fun finishPractice() {
        if (sending) return
        recordCurrentIfNeeded()
        if (results.isEmpty()) {
            back()
            return
        }

        sending = true
        msg = "Sending your written practice..."
        msgError = false
        val correct = results.count { it.correct }
        val report = buildReport()

        sendHomeworkReportToTeacher(
            studentName = studentName,
            lessonName = name,
            homeworkType = "Written practice",
            scoreText = "$correct / ${results.size}",
            report = report
        ) { success ->
            Handler(Looper.getMainLooper()).post {
                sending = false
                if (success) {
                    back()
                } else {
                    msg = "Failed to send practice report. Please try again."
                    msgError = true
                }
            }
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .hideKeyboardOnBackgroundTap(focusManager, keyboard)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name, "Written practice")

        if (msg.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = if (msgError) writtenPracticeRed else writtenPracticeGreen,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(shadow = Shadow(Color.White, Offset(1f, 1f), 3f))
            )
        }

        Spacer(Modifier.height(14.dp))

        currentSentence()?.let { sentence ->
            WrittenPracticeCard(
                number = if (sentences.isEmpty()) 0 else (results.size % sentences.size) + 1,
                total = sentences.size,
                sentence = sentence,
                answer = answer,
                hintState = hintState,
                enabled = !sending,
                changeAnswer = { answer = it },
                openHints = {
                    if (hintState.isBlank()) hintState = "open:"
                },
                chooseHintWord = { index ->
                    hintState = addPracticeHintIndex(hintState, index)
                }
            )
        }

        Spacer(Modifier.height(14.dp))

        ArtButton(
            text = "Next sentence",
            onClick = { nextSentence() },
            modifier = Modifier.fillMaxWidth(0.85f),
            backgroundResId = R.drawable.graybutton,
            enabled = !sending,
            fontSize = 16
        )

        Spacer(Modifier.height(8.dp))

        ArtButton(
            text = if (sending) "Sending..." else "Back",
            onClick = { finishPractice() },
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton,
            enabled = !sending
        )
    }
}

@Composable
private fun WrittenPracticeCard(
    number: Int,
    total: Int,
    sentence: WrittenHomeworkSentence,
    answer: String,
    hintState: String,
    enabled: Boolean,
    changeAnswer: (String) -> Unit,
    openHints: () -> Unit,
    chooseHintWord: (Int) -> Unit
) {
    val ok = isCorrectAnswer(answer, sentence.english)
    val selectedHints = selectedPracticeHintIndexes(hintState)
    val tokens = practiceTokens(sentence.english)

    Card(Modifier.fillMaxWidth(), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$number / $total. ${sentence.portuguese}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (answer.isNotBlank()) {
                    Text(if (ok) "✓" else "X", color = if (ok) writtenPracticeGreen else writtenPracticeRed, fontSize = 54.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            TextField(
                value = answer,
                onValueChange = changeAnswer,
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            )

            if (answer.isNotBlank()) {
                Spacer(Modifier.height(6.dp))
                Text(
                    text = practiceColoredAnswer(answer, sentence.english, writtenPracticeRed),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(8.dp))

            if (hintState.isBlank()) {
                ArtButton(
                    text = "Choose a spoiler word",
                    onClick = openHints,
                    modifier = Modifier.fillMaxWidth(0.75f),
                    backgroundResId = R.drawable.graybutton,
                    enabled = enabled,
                    heightDp = 52,
                    fontSize = 15
                )
            } else {
                Text("Choose a word to reveal.")
                Spacer(Modifier.height(5.dp))
                Row(Modifier.fillMaxWidth()) {
                    tokens.forEachIndexed { index, token ->
                        val revealed = selectedHints.contains(index)
                        Text(
                            text = if (revealed) token else practiceUnderlineToken(token),
                            color = if (revealed) Color(0xFF0D3D7A) else Color(0xFF555555),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 4.dp)
                                .clickable(enabled = enabled && !revealed) { chooseHintWord(index) }
                        )
                    }
                }
                Text("Spoilers used: ${selectedHints.size}")
            }
        }
    }
}

private fun formatPracticeDuration(ms: Long): String {
    val totalSeconds = (ms / 1000).coerceAtLeast(0)
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "${minutes}m ${seconds}s"
}

private fun selectedPracticeHintIndexes(state: String): Set<Int> {
    if (!state.startsWith("open:")) return emptySet()
    val raw = state.removePrefix("open:")
    if (raw.isBlank()) return emptySet()
    return raw.split(",").mapNotNull { it.toIntOrNull() }.toSet()
}

private fun addPracticeHintIndex(state: String, index: Int): String {
    val selected = selectedPracticeHintIndexes(state).toMutableSet()
    selected.add(index)
    return "open:" + selected.sorted().joinToString(",")
}

private fun practiceTokens(text: String): List<String> = text.trim().split(" ").filter { it.isNotBlank() }

private fun practiceUnderlineToken(token: String): String = "____" + token.takeLastWhile { !it.isLetterOrDigit() }

private fun practiceColoredAnswer(answer: String, expected: String, wrongColor: Color) = buildAnnotatedString {
    val expectedWords = cleanAnswer(expected).split(" ").filter { it.isNotBlank() }
    val ranges = practiceWordRanges(answer)
    val studentWords = ranges.map { cleanAnswer(answer.substring(it.first, it.last)) }
    val matchMap = practiceMatchingStudentToExpected(studentWords, expectedWords)
    var expectedCursor = 0

    for (i in ranges.indices) {
        val expectedIndex = matchMap[i]
        if (expectedIndex != null && expectedIndex >= expectedCursor) {
            while (expectedCursor < expectedIndex) {
                appendPracticeMissingUnderline(wrongColor)
                expectedCursor++
            }
        }

        val start = length
        append(answer.substring(ranges[i].first, ranges[i].last))
        val end = length
        if (studentWords[i].isNotBlank() && expectedIndex == null) {
            addStyle(SpanStyle(color = wrongColor), start, end)
            if (expectedCursor < expectedWords.size) expectedCursor++
        }
        append(" ")
        if (expectedIndex != null && expectedIndex >= expectedCursor) expectedCursor = expectedIndex + 1
    }

    while (expectedCursor < expectedWords.size) {
        appendPracticeMissingUnderline(wrongColor)
        expectedCursor++
    }
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.appendPracticeMissingUnderline(color: Color) {
    val start = length
    append("____ ")
    addStyle(SpanStyle(color = color, textDecoration = TextDecoration.Underline), start, length)
}

private fun practiceMatchingStudentToExpected(studentWords: List<String>, expectedWords: List<String>): Map<Int, Int> {
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

private fun practiceWordRanges(text: String): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    var start = -1
    for (i in text.indices) {
        val word = text[i].isLetterOrDigit() || text[i] == '\u2019' || text[i] == '\''
        if (word && start == -1) start = i
        if (!word && start != -1) {
            ranges.add(IntRange(start, i))
            start = -1
        }
    }
    if (start != -1) ranges.add(IntRange(start, text.length))
    return ranges
}
