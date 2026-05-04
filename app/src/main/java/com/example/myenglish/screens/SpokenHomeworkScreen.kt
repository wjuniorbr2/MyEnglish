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
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

private val spokenCorrectMessages = arrayOf(
    "Nailed it. The microphone is clapping.",
    "English escaped your mouth perfectly.",
    "The robot ear approves.",
    "Boom. Spoken like a tiny legend.",
    "That sentence just got a trophy.",
    "Your voice did homework. Respect.",
    "Speech wizard activated.",
    "The grammar goblin is impressed."
)

private val spokenWrongMessages = arrayOf(
    "Almost! One word is doing parkour.",
    "The robot ear heard drama. Try again.",
    "Close, but English wants a rematch.",
    "That sentence needs a tiny rescue mission.",
    "A word escaped. Catch it with your voice.",
    "Not yet, brave microphone warrior.",
    "English said: nice try, now again.",
    "The red words are being dramatic."
)

private val spokenHintButtons = arrayOf(
    "Choose a spoiler word",
    "Open clue slots",
    "Summon word blanks",
    "Deploy hint underlines",
    "Call the clue goblin",
    "Unlock tiny mercy"
)

@Composable
fun SpokenHomework(
    name: String,
    studentName: String,
    sentences: Array<SpokenHomeworkSentence>,
    done: () -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("my_english_prefs", android.content.Context.MODE_PRIVATE) }
    val scroll = rememberScrollState()

    val answers = remember(sentences.size) { mutableStateListOf<String>().apply { repeat(sentences.size) { add("") } } }
    val attempts = remember(sentences.size) { mutableStateListOf<Int>().apply { repeat(sentences.size) { add(0) } } }
    val hintStates = remember(sentences.size) { mutableStateListOf<String>().apply { repeat(sentences.size) { add("") } } }

    var activeIndex by remember { mutableIntStateOf(-1) }
    var msg by remember { mutableStateOf("") }
    var sending by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    var showInstructions by remember { mutableStateOf(false) }
    var scrollTopRequest by remember { mutableIntStateOf(0) }
    var ttsReady by remember { mutableStateOf(false) }

    val textToSpeech = remember {
        TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsReady = true
            }
        }
    }

    LaunchedEffect(ttsReady) {
        if (ttsReady) {
            val result = textToSpeech.setLanguage(Locale.US)
            ttsReady = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
            textToSpeech.setSpeechRate(0.85f)
        }
    }

    LaunchedEffect(scrollTopRequest) {
        if (scrollTopRequest > 0) scroll.animateScrollTo(0)
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }

    fun speakHintWord(word: String) {
        if (ttsReady && word.isNotBlank()) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, "hint_word_$word")
        } else {
            msg = "This phone is not ready to pronounce English yet. The tiny speaker needs English magic installed."
            scrollTopRequest++
        }
    }

    val instructionsSeenKey = "spoken_homework_instructions_seen"

    LaunchedEffect(Unit) {
        if (!prefs.getBoolean(instructionsSeenKey, false)) {
            showInstructions = true
        }
    }

    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val index = activeIndex
        activeIndex = -1

        if (index !in sentences.indices) return@rememberLauncherForActivityResult

        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data
                ?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                ?.firstOrNull()
                ?.trim()
                ?: ""

            if (spokenText.isNotBlank()) {
                attempts[index] = attempts[index] + 1
                answers[index] = applyExpectedPunctuation(spokenText, sentences[index].english)

                msg = if (isCorrectAnswer(answers[index], sentences[index].english)) {
                    spokenCorrectMessages[index % spokenCorrectMessages.size]
                } else {
                    spokenWrongMessages[index % spokenWrongMessages.size]
                }
            } else {
                msg = "The microphone heard mysterious silence. Try again."
            }
        } else {
            msg = "The robot ear gave up too early. Try again."
        }
    }

    fun launchSpeech(index: Int) {
        activeIndex = index
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
        val index = activeIndex
        if (granted && index in sentences.indices) {
            launchSpeech(index)
        } else {
            activeIndex = -1
            msg = "Microphone permission is needed. The robot ear cannot work without ears."
        }
    }

    fun startSpeaking(index: Int) {
        activeIndex = index
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            launchSpeech(index)
        } else {
            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    fun allCorrect(): Boolean {
        if (sentences.isEmpty()) return false
        var i = 0
        while (i < sentences.size) {
            if (!isCorrectAnswer(answers[i], sentences[i].english)) return false
            i++
        }
        return true
    }

    fun buildReport(): String {
        val builder = StringBuilder()
        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Submitted at: ").append(currentDateTimeText()).append("\n")
        builder.append("Lesson: ").append(name).append("\n")
        builder.append("Homework: Spoken homework\n\n")

        var i = 0
        while (i < sentences.size) {
            builder.append(i + 1).append(". Portuguese: ").append(sentences[i].portuguese).append("\n")
            builder.append("Expected English: ").append(sentences[i].english).append("\n")
            builder.append("Recognized speech: ").append(answers[i]).append("\n")
            builder.append("Attempts: ").append(attempts[i]).append("\n")
            builder.append("Hints used: ").append(selectedHintIndexes(hintStates[i]).size).append("\n\n")
            i++
        }

        return builder.toString()
    }

    fun submit() {
        if (!allCorrect()) {
            msg = "The red words are still partying. Fix every sentence first."
            scrollTopRequest++
            return
        }

        sending = true
        msg = "Sending your spoken masterpiece..."
        scrollTopRequest++
        val report = buildReport()

        sendHomeworkReportToTeacher(
            studentName = studentName,
            lessonName = name,
            homeworkType = "Spoken homework",
            scoreText = "${sentences.size} / ${sentences.size}",
            report = report
        ) { success ->
            Handler(Looper.getMainLooper()).post {
                sending = false
                if (success) {
                    submitted = true
                    done()
                    msg = "Boom! Your spoken masterpiece reached your teacher."
                    scrollTopRequest++
                } else {
                    msg = "Failed to send report. The internet gremlin might be hungry. Try again."
                    scrollTopRequest++
                }
            }
        }
    }

    if (showInstructions) {
        SpokenInstructionsDialog(
            close = {
                prefs.edit().putBoolean(instructionsSeenKey, true).apply()
                showInstructions = false
            }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name, "Spoken")

        Spacer(Modifier.height(8.dp))

        ArtButton(
            text = "Instructions",
            onClick = { showInstructions = true },
            modifier = Modifier.fillMaxWidth(0.55f),
            backgroundResId = R.drawable.graybutton,
            heightDp = 48,
            fontSize = 15
        )

        if (msg.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = Color(0xFF555555),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.White, Offset(1f, 1f), 3f)
                )
            )
        }

        Spacer(Modifier.height(16.dp))

        var i = 0
        while (i < sentences.size) {
            val index = i
            SpokenSentenceCard(
                number = index + 1,
                sentence = sentences[index],
                answer = answers[index],
                attempts = attempts[index],
                hintState = hintStates[index],
                submitted = submitted,
                speak = { startSpeaking(index) },
                openHints = {
                    if (attempts[index] >= 5 && hintStates[index].isBlank()) {
                        hintStates[index] = "open:"
                    }
                },
                chooseHintWord = { wordIndex ->
                    hintStates[index] = addHintIndex(hintStates[index], wordIndex)
                },
                hearHintWord = { word -> speakHintWord(word) },
                messageIndex = index
            )
            Spacer(Modifier.height(12.dp))
            i++
        }

        ArtButton(
            text = if (submitted) "Submitted" else if (sending) "Sending..." else "Voice mission complete",
            onClick = { if (!sending && !submitted) submit() },
            enabled = !sending && !submitted,
            modifier = Modifier.fillMaxWidth(0.85f),
            backgroundResId = R.drawable.graybutton,
            fontSize = 16
        )

        Spacer(Modifier.height(16.dp))

        ArtButton(
            text = "Back",
            onClick = back,
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}

@Composable
private fun SpokenSentenceCard(
    number: Int,
    sentence: SpokenHomeworkSentence,
    answer: String,
    attempts: Int,
    hintState: String,
    submitted: Boolean,
    speak: () -> Unit,
    openHints: () -> Unit,
    chooseHintWord: (Int) -> Unit,
    hearHintWord: (String) -> Unit,
    messageIndex: Int
) {
    val currentOk = isCorrectAnswer(answer, sentence.english)
    val selectedHints = selectedHintIndexes(hintState)
    val hintTokens = expectedDisplayTokens(sentence.english)
    val wordsCorrectButWrongOrder = answer.isNotBlank() && !currentOk && sameCleanWordsDifferentOrder(answer, sentence.english)

    Card(
        Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$number. ${sentence.portuguese}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                if (answer.isNotBlank()) {
                    Text(
                        text = if (currentOk) "✓" else "✕",
                        color = if (currentOk) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 54.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            ArtButton(
                text = "🎙 Speak",
                onClick = speak,
                modifier = Modifier.fillMaxWidth(0.55f),
                heightDp = 54,
                fontSize = 17,
                enabled = !submitted
            )

            Spacer(Modifier.height(8.dp))

            Text(
                text = "Android heard:",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF555555)
            )

            Spacer(Modifier.height(4.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF4F4F4), MaterialTheme.shapes.small)
                    .padding(10.dp)
            ) {
                if (answer.isNotBlank()) {
                    Text(
                        text = coloredAnswer(answer, sentence.english),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(4.dp))
            Text("Tries: $attempts / 5")

            if (answer.isNotBlank()) {
                Text(
                    text = if (currentOk) {
                        spokenCorrectMessages[messageIndex % spokenCorrectMessages.size]
                    } else if (wordsCorrectButWrongOrder) {
                        "The order is incorrect."
                    } else {
                        spokenWrongMessages[messageIndex % spokenWrongMessages.size]
                    }
                )
            }

            if (!currentOk && attempts >= 5 && !submitted && hintState.isBlank()) {
                Spacer(Modifier.height(6.dp))
                ArtButton(
                    text = spokenHintButtons[messageIndex % spokenHintButtons.size],
                    onClick = openHints,
                    modifier = Modifier.fillMaxWidth(0.75f),
                    backgroundResId = R.drawable.graybutton,
                    heightDp = 52,
                    fontSize = 15
                )
            } else if (!currentOk && attempts in 1..4) {
                Text("Speak ${5 - attempts} more time(s) to unlock the clue goblin.")
            }

            if (hintState.startsWith("open:")) {
                Spacer(Modifier.height(8.dp))
                Text("Choose a word to reveal. Tap revealed words to hear them.")
                Spacer(Modifier.height(5.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    var i = 0
                    while (i < hintTokens.size) {
                        val wordIndex = i
                        val token = hintTokens[wordIndex]
                        val revealed = selectedHints.contains(wordIndex)
                        Text(
                            text = if (revealed) token else underlineToken(token),
                            color = if (revealed) Color(0xFF0D3D7A) else Color(0xFF555555),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 4.dp)
                                .clickable {
                                    if (revealed) {
                                        hearHintWord(cleanAnswer(token))
                                    } else {
                                        chooseHintWord(wordIndex)
                                    }
                                }
                        )
                        i++
                    }
                }

                Text("Spoilers used: ${selectedHints.size}")
            }
        }
    }
}

@Composable
private fun SpokenInstructionsDialog(close: () -> Unit) {
    val configuration = LocalConfiguration.current
    val maxDialogHeight = (configuration.screenHeightDp * 0.86f).dp
    val instructionScroll = rememberScrollState()

    Dialog(
        onDismissRequest = close,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxDialogHeight)
                .background(Color(0xFFF4F4F4), MaterialTheme.shapes.large)
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Como funciona esta tarefa",
                color = Color(0xFF0D3D7A),
                fontSize = 21.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "1. Leia a frase em português.\n\n" +
                            "2. Toque no microfone e fale a resposta em inglês.\n\n" +
                            "3. O aplicativo vai escrever o que entendeu, mas você não poderá digitar a resposta.\n\n" +
                            "4. As palavras corretas ficam em preto. As palavras incorretas aparecem em vermelho.\n\n" +
                            "5. Se faltar uma palavra, o aplicativo mostra um espaço sublinhado em vermelho no lugar dela.\n\n" +
                            "6. Pontuação e letras maiúsculas são ajustadas automaticamente pelo aplicativo.\n\n" +
                            "7. Depois de 5 tentativas, o botão de dica aparece. Você escolhe qual palavra quer revelar. Depois, pode tocar na palavra revelada para ouvir a pronúncia.\n\n" +
                            "Importante: faça a atividade em um lugar silencioso, sem TV, música, conversa ou barulho de fundo. Esta tarefa usa reconhecimento de voz em inglês. Fale em inglês, de preferência perto do microfone, e faça a atividade com internet.",
                    color = Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 17.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 20.dp)
                        .verticalScroll(instructionScroll)
                )

                Text(
                    text = "▲\n│\n│\n▼",
                    color = Color(0xFF0D3D7A),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(Color(0xCCF4F4F4), RoundedCornerShape(8.dp))
                        .padding(horizontal = 3.dp, vertical = 5.dp)
                )
            }

            Spacer(Modifier.height(14.dp))

            ArtButton(
                text = "Entendi! Bora falar inglês 🎙",
                onClick = close,
                modifier = Modifier.fillMaxWidth(),
                heightDp = 54,
                fontSize = 15
            )
        }
    }
}

private fun applyExpectedPunctuation(raw: String, expected: String): String {
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

private fun coloredAnswer(answer: String, expected: String) = buildAnnotatedString {
    val expectedWords = cleanAnswer(expected).split(" ").filter { it.isNotBlank() }
    val studentRanges = wordRanges(answer)
    val studentWords = studentRanges.map { cleanAnswer(answer.substring(it.first, it.last)) }
    val matchMap = matchingStudentToExpected(studentWords, expectedWords)

    var expectedCursor = 0
    var i = 0

    while (i < studentRanges.size) {
        val range = studentRanges[i]
        val studentWord = studentWords[i]
        val expectedIndex = matchMap[i]

        if (expectedIndex != null && expectedIndex >= expectedCursor) {
            while (expectedCursor < expectedIndex && noWrongWordCanFillExpectedSlot(i, expectedIndex, matchMap)) {
                appendMissingUnderline()
                expectedCursor++
            }
        }

        val start = length
        append(answer.substring(range.first, range.last))
        val end = length

        if (studentWord.isNotBlank() && expectedIndex == null) {
            addStyle(
                style = SpanStyle(color = Color(0xFFC62828)),
                start = start,
                end = end
            )
            if (expectedCursor < expectedWords.size) {
                expectedCursor++
            }
        }

        append(" ")
        if (expectedIndex != null && expectedIndex >= expectedCursor) {
            expectedCursor = expectedIndex + 1
        }
        i++
    }

    while (expectedCursor < expectedWords.size) {
        appendMissingUnderline()
        expectedCursor++
    }
}

private fun noWrongWordCanFillExpectedSlot(
    currentStudentIndex: Int,
    matchedExpectedIndex: Int,
    matchMap: Map<Int, Int>
): Boolean {
    var i = currentStudentIndex - 1
    while (i >= 0) {
        if (!matchMap.containsKey(i)) return false
        val previousExpectedIndex = matchMap[i] ?: -1
        if (previousExpectedIndex < matchedExpectedIndex) return true
        i--
    }
    return true
}

private fun androidx.compose.ui.text.AnnotatedString.Builder.appendMissingUnderline() {
    val start = length
    append("____ ")
    val end = length
    addStyle(
        style = SpanStyle(
            color = Color(0xFFC62828),
            textDecoration = TextDecoration.Underline
        ),
        start = start,
        end = end
    )
}

private fun matchingStudentToExpected(studentWords: List<String>, expectedWords: List<String>): Map<Int, Int> {
    val result = mutableMapOf<Int, Int>()
    val usedExpectedIndexes = mutableSetOf<Int>()

    var i = 0
    while (i < studentWords.size) {
        var found = -1
        var j = 0
        while (j < expectedWords.size && found == -1) {
            if (!usedExpectedIndexes.contains(j) && studentWords[i] == expectedWords[j]) {
                found = j
            }
            j++
        }

        if (found != -1) {
            result[i] = found
            usedExpectedIndexes.add(found)
        }
        i++
    }

    return result
}

private fun sameCleanWordsDifferentOrder(answer: String, expected: String): Boolean {
    val answerWords = cleanAnswer(answer).split(" ").filter { it.isNotBlank() }
    val expectedWords = cleanAnswer(expected).split(" ").filter { it.isNotBlank() }

    if (answerWords.size != expectedWords.size) return false
    if (answerWords == expectedWords) return false
    return answerWords.sorted() == expectedWords.sorted()
}

private fun selectedHintIndexes(state: String): Set<Int> {
    if (!state.startsWith("open:")) return emptySet()
    val raw = state.removePrefix("open:")
    if (raw.isBlank()) return emptySet()

    return raw.split(",")
        .mapNotNull { it.toIntOrNull() }
        .toSet()
}

private fun addHintIndex(state: String, index: Int): String {
    val selected = selectedHintIndexes(state).toMutableSet()
    selected.add(index)
    return "open:" + selected.sorted().joinToString(",")
}

private fun expectedDisplayTokens(text: String): List<String> {
    return text.trim().split(" ").filter { it.isNotBlank() }
}

private fun underlineToken(token: String): String {
    val punctuation = token.takeLastWhile { !it.isLetterOrDigit() }
    return "____$punctuation"
}

private fun wordRanges(text: String): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    var start = -1
    var i = 0

    while (i < text.length) {
        val isWordChar = text[i].isLetterOrDigit() || text[i] == '’' || text[i] == '\''

        if (isWordChar && start == -1) start = i
        if (!isWordChar && start != -1) {
            ranges.add(IntRange(start, i))
            start = -1
        }
        i++
    }

    if (start != -1) ranges.add(IntRange(start, text.length))
    return ranges
}
