package com.example.myenglish.screens

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.R
import com.example.myenglish.components.ArtButton
import com.example.myenglish.data.BookAudioItem
import com.example.myenglish.data.Lesson1BookData

@Composable
fun BookScreen(
    lessonName: String,
    back: () -> Unit
) {
    val context = LocalContext.current
    val handler = remember { Handler(Looper.getMainLooper()) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    var showGrammarInfo by remember { mutableStateOf(false) }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        try { player?.stop() } catch (_: Exception) { }
        try { player?.release() } catch (_: Exception) { }
        player = null
    }

    fun playSegment(item: BookAudioItem) {
        stop()
        val currentPlayer = MediaPlayer.create(context, item.audioResId) ?: return
        player = currentPlayer
        currentPlayer.setVolume(1f, 1f)
        currentPlayer.seekTo(item.startMs)
        currentPlayer.start()
        handler.postDelayed({ if (player == currentPlayer) stop() }, (item.endMs - item.startMs).toLong())
    }

    fun playFull(resId: Int) {
        stop()
        val currentPlayer = MediaPlayer.create(context, resId) ?: return
        player = currentPlayer
        currentPlayer.setVolume(1f, 1f)
        currentPlayer.start()
        currentPlayer.setOnCompletionListener { stop() }
    }

    DisposableEffect(Unit) {
        onDispose { stop() }
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
                AudioTitle(Lesson1BookData.title) { playSegment(Lesson1BookData.title) }
                ArtButton(
                    text = "▶ All",
                    onClick = { playFull(Lesson1BookData.BOOK_AUDIO_RES_ID) },
                    modifier = Modifier.width(86.dp),
                    heightDp = 46,
                    fontSize = 14
                )
            }

            Spacer(Modifier.height(10.dp))

            FramedSection {
                SectionTitle(Lesson1BookData.verbsTitle) { playSegment(Lesson1BookData.verbsTitle) }
                WordGrid(Lesson1BookData.verbs, 3, ::playSegment)
            }

            FramedSection {
                SectionTitle(Lesson1BookData.vocabularyTitle) { playSegment(Lesson1BookData.vocabularyTitle) }
                WordGrid(Lesson1BookData.vocabulary, 3, ::playSegment)
            }

            FramedSection {
                SectionTitle(Lesson1BookData.expressionsTitle) { playSegment(Lesson1BookData.expressionsTitle) }
                WordGrid(Lesson1BookData.expressions, 2, ::playSegment)
            }

            FramedSection {
                Box(Modifier.fillMaxWidth()) {
                    SectionTitle(Lesson1BookData.grammarTitle) {
                        playSegment(Lesson1BookData.grammarTitle)
                        showGrammarInfo = true
                    }

                    if (showGrammarInfo) {
                        GrammarBalloon(
                            modifier = Modifier
                                .align(Alignment.TopStart)
                                .offset(x = 4.dp, y = 38.dp)
                        ) {
                            showGrammarInfo = false
                        }
                    }
                }

                Text(
                    text = "Presente nas formas + positiva, - negativa, ? interrogativa e ?- interrogativa negativa",
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp, bottom = 3.dp)
                )
                WordGrid(Lesson1BookData.grammarSentences, 2, ::playSegment)
            }

            Row(
                Modifier.fillMaxWidth().padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ALPHABET",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier
                        .background(Color(0x99000000), RoundedCornerShape(8.dp))
                        .clickable { playSegment(Lesson1BookData.alphabetTitle) }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
                ArtButton(
                    text = "▶ ABC",
                    onClick = { playFull(Lesson1BookData.ALPHABET_AUDIO_RES_ID) },
                    modifier = Modifier.width(95.dp),
                    heightDp = 46,
                    fontSize = 14
                )
            }
            AlphabetGrid(Lesson1BookData.alphabet, ::playSegment)

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
}

@Composable
private fun FramedSection(content: @Composable () -> Unit) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
            .background(Color(0xAA111111), RoundedCornerShape(12.dp))
            .border(BorderStroke(2.dp, Color(0xFF555555)), RoundedCornerShape(12.dp))
            .padding(7.dp)
    ) {
        content()
    }
}

@Composable
private fun GrammarBalloon(modifier: Modifier, close: () -> Unit) {
    Box(
        modifier
            .width(315.dp)
            .clickable { close() }
    ) {
        Canvas(Modifier.matchParentSize().height(120.dp)) {
            val path = Path().apply {
                moveTo(18f, 18f)
                quadraticBezierTo(18f, 0f, 36f, 0f)
                lineTo(size.width - 18f, 0f)
                quadraticBezierTo(size.width, 0f, size.width, 18f)
                lineTo(size.width, 88f)
                quadraticBezierTo(size.width, 106f, size.width - 18f, 106f)
                lineTo(45f, 106f)
                lineTo(18f, 128f)
                lineTo(26f, 106f)
                lineTo(18f, 106f)
                quadraticBezierTo(0f, 106f, 0f, 88f)
                lineTo(0f, 18f)
                quadraticBezierTo(0f, 0f, 18f, 0f)
                close()
            }
            drawPath(path, Color(0xFFF4F4F4))
            drawPath(path, Color(0xFF555555), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f))
        }

        Text(
            text = "Aqui estamos aprendendo o presente de algumas frases nas formas positiva, negativa, interrogativa e interrogativa negativa.",
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 17.sp,
            modifier = Modifier.padding(start = 12.dp, top = 10.dp, end = 12.dp, bottom = 22.dp)
        )
    }
}

@Composable
private fun AudioTitle(item: BookAudioItem, onClick: () -> Unit) {
    Text(
        text = item.english,
        color = Color.White,
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
        modifier = Modifier
            .background(Color(0xAA000000), RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 5.dp)
    )
}

@Composable
private fun SectionTitle(item: BookAudioItem, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Text(
        text = item.english,
        color = Color.White,
        fontSize = 19.sp,
        fontWeight = FontWeight.Black,
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
                if (index < items.size) {
                    WordCell(items[index], Modifier.weight(1f), play)
                } else {
                    Spacer(Modifier.weight(1f))
                }
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
    Box(
        modifier
            .clickable { play(item) }
            .padding(5.dp)
    ) {
        Text(
            text = item.english + if (item.translation != "") " - " + item.translation else "",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 17.sp
        )
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
                if (index < items.size) {
                    AlphabetCell(items[index], play)
                } else {
                    Spacer(Modifier.width(42.dp))
                }
                col++
            }
        }
        Spacer(Modifier.height(5.dp))
        i += 7
    }
}

@Composable
private fun AlphabetCell(item: BookAudioItem, play: (BookAudioItem) -> Unit) {
    Column(
        modifier = Modifier
            .width(42.dp)
            .background(Color(0xAA000000), RoundedCornerShape(6.dp))
            .padding(3.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = item.english,
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            modifier = Modifier.clickable { play(item) }
        )
        Text(
            text = item.translation,
            color = Color.White,
            fontSize = 9.sp
        )
    }
}
