package com.example.myenglish.screens

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.myenglish.R
import com.example.myenglish.components.ArtButton
import com.example.myenglish.data.BookAudioItem
import com.example.myenglish.data.BookLessonData
import com.example.myenglish.data.Lesson1BookData
import com.example.myenglish.data.Lesson2BookData
import com.example.myenglish.data.Lesson3BookData
import com.example.myenglish.data.Lesson4BookData
import com.example.myenglish.data.Lesson5BookData
import com.example.myenglish.data.Lesson6BookData
import java.util.Locale

private val frameOuterColor = Color(0xFF0D3D7A)
private val frameInnerColor = Color(0xFF2E75C9)
private val darkPanelColor = Color(0xAA111111)

@Composable
fun BookScreen(
    lessonName: String,
    back: () -> Unit
) {
    val bookData: BookLessonData = when (lessonName) {
        "Lesson 2" -> Lesson2BookData
        "Lesson 3" -> Lesson3BookData
        "Lesson 4" -> Lesson4BookData
        "Lesson 5" -> Lesson5BookData
        "Lesson 6" -> Lesson6BookData
        else -> Lesson1BookData
    }

    val context = LocalContext.current
    val handler = remember { Handler(Looper.getMainLooper()) }
    val playerRef = remember { arrayOf<MediaPlayer?>(null) }
    var showGrammarInfo by rememberSaveable(lessonName) { mutableStateOf(false) }
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

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        try { playerRef[0]?.stop() } catch (_: Exception) { }
        try { playerRef[0]?.release() } catch (_: Exception) { }
        playerRef[0] = null
        textToSpeech.stop()
    }

    fun speakText(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH, utteranceId: String = "book_tts") {
        if (!ttsReady || text.isBlank()) return
        textToSpeech.speak(text, queueMode, null, utteranceId)
    }

    fun playSegment(item: BookAudioItem) {
        stop()
        if (item.audioResId == 0) {
            speakText(item.english, TextToSpeech.QUEUE_FLUSH, "book_${item.english}")
            return
        }
        val currentPlayer = MediaPlayer.create(context, item.audioResId) ?: return
        playerRef[0] = currentPlayer
        currentPlayer.setVolume(1f, 1f)

        val isAlphabetItem = item.audioResId == bookData.alphabetAudioResId && item.english != "THE ALPHABET"
        val beforeBuffer = when {
            item.useExactTiming -> 0
            isAlphabetItem -> 90
            else -> 260
        }
        val afterBuffer = when {
            item.useExactTiming -> 0
            isAlphabetItem -> 230
            else -> 760
        }
        val bufferedStart = (item.startMs - beforeBuffer).coerceAtLeast(0)
        val duration = (item.endMs - item.startMs + beforeBuffer + afterBuffer).coerceAtLeast(350)

        currentPlayer.seekTo(bufferedStart)
        currentPlayer.start()
        handler.postDelayed({ if (playerRef[0] == currentPlayer) stop() }, duration.toLong())
    }

    fun playFull(resId: Int) {
        stop()
        if (resId == 0) {
            val chunks = mutableListOf<String>()
            chunks.add(bookData.title.english)
            chunks.add(bookData.verbsTitle.english)
            bookData.verbs.forEach { chunks.add(it.english) }
            chunks.add(bookData.vocabularyTitle.english)
            bookData.vocabulary.forEach { chunks.add(it.english) }
            chunks.add(bookData.expressionsTitle.english)
            bookData.expressions.forEach { chunks.add(it.english) }
            chunks.add(bookData.grammarTitle.english)
            bookData.grammarSentences.forEach { chunks.add(it.english) }
            chunks.forEachIndexed { index, text ->
                speakText(text, if (index == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD, "book_all_$index")
            }
            return
        }
        val currentPlayer = MediaPlayer.create(context, resId) ?: return
        playerRef[0] = currentPlayer
        currentPlayer.setVolume(1f, 1f)
        currentPlayer.start()
        currentPlayer.setOnCompletionListener { stop() }
    }

    DisposableEffect(Unit) {
        onDispose {
            stop()
            textToSpeech.shutdown()
        }
    }

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.screenbg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(7.dp),
            contentScale = ContentScale.Crop
        )

        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(Modifier.width(68.dp))
                AudioTitle(bookData.title) { playSegment(bookData.title) }
                ArtButton(
                    text = "▶ All",
                    onClick = { playFull(bookData.bookAudioResId) },
                    modifier = Modifier.width(86.dp),
                    heightDp = 46,
                    fontSize = 14
                )
            }

            Spacer(Modifier.height(10.dp))

            FramedSection {
                SectionTitle(bookData.verbsTitle) { playSegment(bookData.verbsTitle) }
                WordGrid(bookData.verbs, 3, ::playSegment)
            }

            FramedSection {
                SectionTitle(bookData.vocabularyTitle) { playSegment(bookData.vocabularyTitle) }
                WordGrid(bookData.vocabulary, 3, ::playSegment)
            }

            FramedSection {
                SectionTitle(bookData.expressionsTitle) { playSegment(bookData.expressionsTitle) }
                WordGrid(bookData.expressions, 2, ::playSegment)
            }

            FramedSection {
                SectionTitle(bookData.grammarTitle) {
                    showGrammarInfo = true
                    playSegment(bookData.grammarTitle)
                }

                if (bookData.grammarNoteText.isNotBlank()) {
                    Text(
                        text = bookData.grammarNoteText,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 5.dp, bottom = 3.dp)
                    )
                }

                WordGrid(bookData.grammarSentences, 2, ::playSegment)
            }

            Row(
                Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .background(darkPanelColor, RoundedCornerShape(12.dp))
                        .border(4.dp, frameOuterColor, RoundedCornerShape(12.dp))
                        .padding(3.dp)
                        .border(2.dp, frameInnerColor, RoundedCornerShape(10.dp))
                        .clickable { playSegment(bookData.alphabetTitle) }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = "ALPHABET",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black
                    )
                }
                ArtButton(
                    text = "▶ ABC",
                    onClick = { playFull(bookData.alphabetAudioResId) },
                    modifier = Modifier.width(95.dp),
                    heightDp = 46,
                    fontSize = 14
                )
            }
            AlphabetGrid(bookData.alphabet, ::playSegment)

            Spacer(Modifier.height(14.dp))
            ArtButton(
                text = "Back",
                onClick = back,
                modifier = Modifier.fillMaxWidth(0.45f),
                backgroundResId = R.drawable.redbutton
            )
            Spacer(Modifier.height(18.dp))
        }
    }

    if (showGrammarInfo) {
        GrammarInfoDialog(
            text = bookData.grammarInfoText,
            close = { showGrammarInfo = false }
        )
    }
}

@Composable
private fun FramedSection(content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .background(darkPanelColor, RoundedCornerShape(14.dp))
            .border(4.dp, frameOuterColor, RoundedCornerShape(14.dp))
            .padding(4.dp)
            .border(2.dp, frameInnerColor, RoundedCornerShape(12.dp))
            .padding(7.dp)
    ) {
        Column { content() }
    }
}

@Composable
private fun GrammarInfoDialog(text: String, close: () -> Unit) {
    val configuration = LocalConfiguration.current
    val maxDialogHeight = (configuration.screenHeightDp * 0.86f).dp
    val textScrollState = rememberScrollState()
    val showScrollHint = text.length > 320

    Dialog(
        onDismissRequest = close,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = maxDialogHeight)
                .background(Color(0xFFF4F4F4), RoundedCornerShape(18.dp))
                .border(4.dp, frameOuterColor, RoundedCornerShape(18.dp))
                .padding(4.dp)
                .border(2.dp, frameInnerColor, RoundedCornerShape(15.dp))
                .padding(14.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("GRAMMAR", color = frameOuterColor, fontSize = 21.sp, fontWeight = FontWeight.Black, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                Box(modifier = Modifier.weight(1f, fill = false).fillMaxWidth()) {
                    Text(
                        text = text,
                        color = Color.Black,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Start,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = if (showScrollHint) 20.dp else 0.dp)
                            .verticalScroll(textScrollState)
                    )
                    if (showScrollHint) {
                        Text(
                            text = "▲\n│\n│\n▼",
                            color = frameOuterColor,
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
                }
                Spacer(Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .background(frameOuterColor, RoundedCornerShape(12.dp))
                        .clickable { close() }
                        .padding(horizontal = 30.dp, vertical = 9.dp)
                ) {
                    Text("OK", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
private fun AudioTitle(item: BookAudioItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .background(darkPanelColor, RoundedCornerShape(12.dp))
            .border(4.dp, frameOuterColor, RoundedCornerShape(12.dp))
            .padding(3.dp)
            .border(2.dp, frameInnerColor, RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 5.dp)
    ) {
        Text(item.english, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Black)
    }
}

@Composable
private fun SectionTitle(item: BookAudioItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        text = item.english,
        color = Color.White,
        fontSize = 19.sp,
        fontWeight = FontWeight.Black,
        textAlign = TextAlign.Center,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 4.dp)
            .clickable { onClick() }
            .padding(3.dp)
    )
}

@Composable
private fun WordGrid(items: Array<BookAudioItem>, columns: Int, play: (BookAudioItem) -> Unit) {
    var i = 0
    while (i < items.size) {
        Row(Modifier.fillMaxWidth()) {
            var col = 0
            while (col < columns) {
                val index = i + col
                if (index < items.size) WordCell(items[index], Modifier.weight(1f), play) else Spacer(Modifier.weight(1f))
                if (col < columns - 1) Spacer(Modifier.width(2.dp))
                col++
            }
        }
        Spacer(Modifier.height(2.dp))
        i += columns
    }
}

@Composable
private fun WordCell(item: BookAudioItem, modifier: Modifier, play: (BookAudioItem) -> Unit) {
    Column(
        modifier = modifier
            .clickable { play(item) }
            .padding(horizontal = 5.dp, vertical = 6.dp)
    ) {
        Text(text = item.english, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, lineHeight = 18.sp)
        if (item.translation.isNotEmpty()) {
            Text(text = item.translation, color = Color(0xFFE0E0E0), fontSize = 11.sp, fontWeight = FontWeight.Normal, lineHeight = 13.sp)
        }
    }
}

@Composable
private fun AlphabetGrid(items: Array<BookAudioItem>, play: (BookAudioItem) -> Unit) {
    var i = 0
    while (i < items.size) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            var col = 0
            while (col < 7) {
                val index = i + col
                if (index < items.size) AlphabetCell(items[index], play) else Spacer(Modifier.width(42.dp))
                col++
            }
        }
        Spacer(Modifier.height(5.dp))
        i += 7
    }
}

@Composable
private fun AlphabetCell(item: BookAudioItem, play: (BookAudioItem) -> Unit) {
    val translationFontSize = if (item.english == "W") 7.sp else 9.sp
    Column(
        modifier = Modifier
            .width(42.dp)
            .background(darkPanelColor, RoundedCornerShape(8.dp))
            .border(3.dp, frameOuterColor, RoundedCornerShape(8.dp))
            .padding(2.dp)
            .border(1.dp, frameInnerColor, RoundedCornerShape(6.dp))
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = item.english, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Black, modifier = Modifier.clickable { play(item) })
        Text(text = item.translation, color = Color(0xFFE0E0E0), fontSize = translationFontSize, maxLines = 1, softWrap = false, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }
}
