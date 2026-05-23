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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
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

private val writtenRed = Color(0xFFC62828)
private val writtenGreen = Color(0xFF2E7D32)

@Composable
fun WrittenHomework(
    name: String,
    studentName: String,
    sentences: Array<WrittenHomeworkSentence>,
    done: () -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", android.content.Context.MODE_PRIVATE) }
    val key = remember(name) { "written_progress_" + name.lowercase().replace(" ", "_") }
    val scroll = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current

    val answers = remember(sentences.size) { mutableStateListOf<String>().apply { repeat(sentences.size) { add("") } } }
    val firstAnswers = remember(sentences.size) { mutableStateListOf<String>().apply { repeat(sentences.size) { add("") } } }
    val hints = remember(sentences.size) { mutableStateListOf<String>().apply { repeat(sentences.size) { add("") } } }

    var step by remember { mutableIntStateOf(0) }
    var firstScore by remember { mutableIntStateOf(0) }
    var msg by remember { mutableStateOf("") }
    var msgError by remember { mutableStateOf(false) }
    var sending by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var topRequest by remember { mutableIntStateOf(0) }

    fun allCorrect(): Boolean = sentences.indices.all { isCorrectAnswer(answers[it], sentences[it].english) }

    fun saveProgress() {
        if (submitted) return
        val edit = prefs.edit()
        edit.putInt("${key}_count", sentences.size)
        edit.putInt("${key}_step", step)
        edit.putInt("${key}_score", firstScore)
        edit.putString("${key}_msg", msg)
        edit.putBoolean("${key}_msg_error", msgError)
        for (i in sentences.indices) {
            edit.putString("${key}_answer_$i", answers[i])
            edit.putString("${key}_first_$i", firstAnswers[i])
            edit.putString("${key}_hint_$i", hints[i])
        }
        edit.apply()
    }

    fun clearProgress() {
        val count = prefs.getInt("${key}_count", sentences.size)
        val edit = prefs.edit()
        edit.remove("${key}_count")
        edit.remove("${key}_step")
        edit.remove("${key}_score")
        edit.remove("${key}_msg")
        edit.remove("${key}_msg_error")
        for (i in 0 until count) {
            edit.remove("${key}_answer_$i")
            edit.remove("${key}_first_$i")
            edit.remove("${key}_hint_$i")
        }
        edit.apply()
    }

    LaunchedEffect(key, sentences.size) {
        if (prefs.getInt("${key}_count", -1) == sentences.size) {
            step = prefs.getInt("${key}_step", 0)
            firstScore = prefs.getInt("${key}_score", 0)
            msg = prefs.getString("${key}_msg", "") ?: ""
            msgError = prefs.getBoolean("${key}_msg_error", false)
            for (i in sentences.indices) {
                answers[i] = prefs.getString("${key}_answer_$i", "") ?: ""
                firstAnswers[i] = prefs.getString("${key}_first_$i", "") ?: ""
                hints[i] = prefs.getString("${key}_hint_$i", "") ?: ""
            }
        }
    }

    LaunchedEffect(scroll.isScrollInProgress) {
        if (scroll.isScrollInProgress) {
            focusManager.clearFocus()
            keyboard?.hide()
        }
    }

    LaunchedEffect(topRequest) {
        if (topRequest > 0) scroll.animateScrollTo(0)
    }

    DisposableEffect(Unit) {
        onDispose { if (submitted) clearProgress() else saveProgress() }
    }

    fun setTop(text: String, error: Boolean) {
        msg = text
        msgError = error
    }

    fun reportText(): String {
        val builder = StringBuilder()
        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Submitted at: ").append(currentDateTimeText()).append("\n")
        builder.append("Lesson: ").append(name).append("\n")
        builder.append("Homework: Written homework\n")
        builder.append("Original score: ").append(firstScore).append(" / ").append(sentences.size).append("\n\n")
        for (i in sentences.indices) {
            builder.append(i + 1).append(". Expected English: ").append(sentences[i].english).append("\n")
            builder.append("First written answer: ").append(firstAnswers[i]).append("\n")
            builder.append("Hints used: ").append(selectedHintIndexes(hints[i]).size).append("\n\n")
        }
        return builder.toString()
    }

    fun checkFirstTry() {
        var score = 0
        for (i in sentences.indices) {
            firstAnswers[i] = answers[i]
            if (isCorrectAnswer(firstAnswers[i], sentences[i].english)) score++
        }
        firstScore = score
        step = 1
        setTop("First try score: $score / ${sentences.size}. Fix the red words, then submit again.", score != sentences.size)
        saveProgress()
        topRequest++
    }

    fun sendFinal() {
        if (!allCorrect()) {
            setTop("The red words are still partying. Fix every sentence first.", true)
            saveProgress()
            topRequest++
            return
        }
        sending = true
        setTop("Sending your written masterpiece...", false)
        val report = reportText()
        saveProgress()
        topRequest++
        sendHomeworkReportToTeacher(studentName, name, "Written homework", "$firstScore / ${sentences.size}", report) { success ->
            Handler(Looper.getMainLooper()).post {
                sending = false
                if (success) {
                    submitted = true
                    setTop("Boom! Your written masterpiece reached your teacher.", false)
                    clearProgress()
                    done()
                    topRequest++
                } else {
                    setTop("Failed to send report. Please try again.", true)
                    saveProgress()
                    topRequest++
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
        Header(name, "Written")
        if (msg.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = if (msgError) writtenRed else writtenGreen,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(shadow = Shadow(Color.White, Offset(1f, 1f), 3f))
            )
        }
        Spacer(Modifier.height(16.dp))
        for (i in sentences.indices) {
            val index = i
            WrittenCard(
                number = index + 1,
                sentence = sentences[index],
                answer = answers[index],
                step = step,
                submitted = submitted,
                hintState = hints[index],
                change = {
                    answers[index] = it
                    saveProgress()
                },
                openHints = {
                    if (!submitted && step >= 1 && hints[index].isBlank()) {
                        hints[index] = "open:"
                        saveProgress()
                    }
                },
                chooseHintWord = { wordIndex ->
                    if (!submitted) {
                        hints[index] = addHintIndex(hints[index], wordIndex)
                        saveProgress()
                    }
                }
            )
            Spacer(Modifier.height(12.dp))
        }
        if (submitted) {
            Button(
                onClick = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(0.85f),
                colors = ButtonDefaults.buttonColors(disabledContainerColor = writtenGreen, disabledContentColor = Color.White)
            ) { Text("Submit written mission") }
        } else {
            ArtButton(
                text = if (sending) "Sending..." else if (step == 0) "Correct me, I dare you!" else "Submit written mission",
                onClick = { if (!sending) { if (step == 0) checkFirstTry() else sendFinal() } },
                enabled = !sending,
                modifier = Modifier.fillMaxWidth(0.85f),
                backgroundResId = R.drawable.graybutton,
                fontSize = 16
            )
        }
        Spacer(Modifier.height(16.dp))
        ArtButton(
            text = "Back",
            onClick = { if (submitted) clearProgress() else saveProgress(); back() },
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}

@Composable
private fun WrittenCard(
    number: Int,
    sentence: WrittenHomeworkSentence,
    answer: String,
    step: Int,
    submitted: Boolean,
    hintState: String,
    change: (String) -> Unit,
    openHints: () -> Unit,
    chooseHintWord: (Int) -> Unit
) {
    val ok = isCorrectAnswer(answer, sentence.english)
    val selectedHints = selectedHintIndexes(hintState)
    val tokens = expectedDisplayTokens(sentence.english)
    Card(Modifier.fillMaxWidth(), border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("$number. ${sentence.portuguese}", style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                if (step >= 1) Text(if (ok) "✓" else "✕", color = if (ok) writtenGreen else writtenRed, fontSize = 54.sp)
            }
            Spacer(Modifier.height(8.dp))
            TextField(
                value = answer,
                onValueChange = change,
                enabled = !submitted,
                visualTransformation = if (step >= 1 && !ok) WrittenCorrectionVisualTransformation(sentence.english) else VisualTransformation.None,
                modifier = Modifier.fillMaxWidth()
            )
            if (step >= 1) {
                Spacer(Modifier.height(4.dp))
                Text(if (ok) "Perfect. Translation unlocked." else "Red words detected. Fix the sentence.", color = if (ok) writtenGreen else writtenRed, fontWeight = FontWeight.Bold)
                if (!ok && !submitted && hintState.isBlank()) {
                    Spacer(Modifier.height(6.dp))
                    ArtButton("Choose a spoiler word", openHints, Modifier.fillMaxWidth(0.75f), R.drawable.graybutton, true, 52, 15)
                }
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
}

private class WrittenCorrectionVisualTransformation(private val correctText: String) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        val expectedWords = cleanAnswer(correctText).split(" ").filter { it.isNotBlank() }
        val ranges = wordRanges(rawText)
        val annotated = buildAnnotatedString {
            append(rawText)
            for (i in ranges.indices) {
                val studentWord = cleanAnswer(rawText.substring(ranges[i].first, ranges[i].last))
                val expectedWord = if (i < expectedWords.size) expectedWords[i] else ""
                if (studentWord.isNotBlank() && studentWord != expectedWord) {
                    addStyle(SpanStyle(color = writtenRed), ranges[i].first, ranges[i].last)
                }
            }
        }
        return TransformedText(annotated, OffsetMapping.Identity)
    }
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
