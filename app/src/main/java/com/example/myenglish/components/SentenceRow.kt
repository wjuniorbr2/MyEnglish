package com.example.myenglish.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.R
import com.example.myenglish.data.HomeworkSentence
import com.example.myenglish.utils.cleanAnswer

private const val HINT_OPEN_FLAG = 1 shl 30

private val correctMessages = arrayOf(
    "Nailed it.",
    "Grammar flex.",
    "Chef's kiss.",
    "Tiny genius.",
    "Too smooth.",
    "English unlocked.",
    "Boom, correct.",
    "Gold star energy.",
    "Flawless victory.",
    "Sentence approved.",
    "The comma bows.",
    "Teacher smiles.",
    "Brain did magic.",
    "No red X today.",
    "Legend behavior."
)

private val incorrectMessages = arrayOf(
    "Red X drama.",
    "Sentence rebelled.",
    "Almost, detective.",
    "Try again, hero.",
    "English said nope.",
    "Close-ish chaos.",
    "Comma is suspicious.",
    "Tiny grammar goblin.",
    "Not yet, champion.",
    "The X has spoken.",
    "Sentence needs snacks.",
    "Oops parade.",
    "Plot twist: no.",
    "Grammar side-eye.",
    "Rescue this one."
)

private val lockedHintMessages = arrayOf(
    "Listen once more. The hint is stretching.",
    "Two more. The hint is hiding behind the couch.",
    "Three more. The hint has entered witness protection.",
    "Four more. The hint is packing a suitcase.",
    "Five listens first. The hint refuses to work overtime."
)

private val revealButtonMessages = arrayOf(
    "Choose a spoiler word",
    "Open clue slots",
    "Summon word blanks",
    "Deploy hint underlines",
    "Call the clue goblin",
    "Unlock tiny mercy",
    "Bribe the sentence",
    "Open secret door"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceRow(
    sentence: HomeworkSentence,
    answer: String,
    change: (String) -> Unit,
    playCount: Int,
    submitStep: Int,
    firstOk: Boolean,
    currentOk: Boolean,
    hintCount: Int,
    focus: FocusRequester,
    inputEnabled: Boolean,
    play: () -> Unit,
    stop: () -> Unit,
    hint: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    messageIndex: Int = 0
) {
    var correctionEdited by remember(submitStep) { mutableStateOf(false) }
    val hintOpen = isHintOpen(hintCount)
    val selectedHints = selectedHintIndexes(hintCount)
    val hintTokens = expectedDisplayTokens(sentence.correctText)

    Card(
        modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row {
                Text(
                    text = sentence.label,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                if (submitStep >= 1) {
                    Text(
                        text = if (submitStep == 2 || currentOk) "✓" else "✕",
                        color = if (submitStep == 2 || currentOk) Color(0xFF2E7D32) else Color(0xFFC62828),
                        fontSize = 64.sp
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Row {
                ArtButton(
                    text = "▶",
                    onClick = play,
                    modifier = Modifier.width(90.dp),
                    heightDp = 56,
                    fontSize = 20
                )

                Spacer(Modifier.width(8.dp))

                ArtButton(
                    text = "Stop",
                    onClick = stop,
                    modifier = Modifier.width(112.dp),
                    backgroundResId = R.drawable.redbutton,
                    heightDp = 56,
                    fontSize = 16
                )
            }

            Spacer(Modifier.height(8.dp))

            TextField(
                value = answer,
                onValueChange = {
                    if (inputEnabled) {
                        if (submitStep == 1) {
                            correctionEdited = true
                        }
                        change(it)
                    }
                },
                enabled = inputEnabled,
                visualTransformation = if (submitStep == 1 && !currentOk) {
                    CorrectionVisualTransformation(
                        correctText = sentence.correctText,
                        includeOpenWord = !correctionEdited
                    )
                } else {
                    VisualTransformation.None
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus)
            )

            Spacer(Modifier.height(4.dp))

            Text("Plays: $playCount")

            if (submitStep >= 1) {
                val attemptMessage = if (firstOk) {
                    correctMessages[messageIndex % correctMessages.size]
                } else {
                    incorrectMessages[messageIndex % incorrectMessages.size]
                }

                Text(
                    text = attemptMessage,
                    color = if (firstOk) Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.Bold
                )

                if (cleanAnswer(answer) == "") {
                    Text("Type first, beg later.")
                } else if (playCount < 5) {
                    val lockedIndex = playCount.coerceIn(0, 4)
                    Text(lockedHintMessages[lockedIndex])
                } else if (submitStep < 2 && !currentOk && !hintOpen) {
                    val wordCount = hintTokens.size
                    val buttonIndex = if (wordCount <= 0) 0 else (wordCount - 1) % revealButtonMessages.size

                    ArtButton(
                        text = revealButtonMessages[buttonIndex],
                        onClick = { hint(null) },
                        modifier = Modifier.fillMaxWidth(0.7f),
                        backgroundResId = R.drawable.graybutton,
                        heightDp = 56,
                        fontSize = 16
                    )
                }

                if (hintOpen) {
                    Spacer(Modifier.height(8.dp))
                    Text("Choose a word to reveal.")
                    Spacer(Modifier.height(5.dp))

                    FlowRow(modifier = Modifier.fillMaxWidth()) {
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
                                        if (!revealed) hint(wordIndex)
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
}

fun openListeningHintState(state: Int): Int {
    return state or HINT_OPEN_FLAG
}

fun addListeningHintIndex(state: Int, index: Int): Int {
    return (state or HINT_OPEN_FLAG) or (1 shl index)
}

fun selectedListeningHintCount(state: Int): Int {
    return Integer.bitCount(state and HINT_OPEN_FLAG.inv())
}

private fun isHintOpen(state: Int): Boolean {
    return (state and HINT_OPEN_FLAG) != 0
}

private fun selectedHintIndexes(state: Int): Set<Int> {
    val selected = mutableSetOf<Int>()
    val cleanState = state and HINT_OPEN_FLAG.inv()
    var i = 0
    while (i < 30) {
        if ((cleanState and (1 shl i)) != 0) selected.add(i)
        i++
    }
    return selected
}

private fun expectedDisplayTokens(text: String): List<String> {
    return text.trim().split(" ").filter { it.isNotBlank() }
}

private fun underlineToken(token: String): String {
    val punctuation = token.takeLastWhile { !it.isLetterOrDigit() }
    return "____$punctuation"
}

private class CorrectionVisualTransformation(
    private val correctText: String,
    private val includeOpenWord: Boolean
) : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val rawText = text.text
        val correctedWords = cleanAnswer(correctText).split(" ").filter { it.isNotEmpty() }
        val ranges = completedWordRanges(rawText, includeOpenWord)

        val annotated = buildAnnotatedString {
            append(rawText)

            var expectedCursor = 0
            for (range in ranges) {
                val studentParts = cleanAnswer(rawText.substring(range.first, range.last))
                    .split(" ")
                    .filter { it.isNotBlank() }
                val endCursor = expectedCursor + studentParts.size
                val matches = studentParts.isNotEmpty() &&
                        endCursor <= correctedWords.size &&
                        correctedWords.subList(expectedCursor, endCursor) == studentParts

                if (studentParts.isNotEmpty() && !matches) {
                    addStyle(
                        style = SpanStyle(color = Color(0xFFC62828)),
                        start = range.first,
                        end = range.last
                    )
                }

                expectedCursor += studentParts.size.coerceAtLeast(1)
            }
        }

        return TransformedText(annotated, OffsetMapping.Identity)
    }
}

private fun completedWordRanges(text: String, includeOpenWord: Boolean): List<IntRange> {
    val ranges = mutableListOf<IntRange>()
    var wordStart = -1
    var i = 0

    while (i < text.length) {
        val isSpace = text[i].isWhitespace()

        if (!isSpace && wordStart == -1) {
            wordStart = i
        }

        if (isSpace && wordStart != -1) {
            ranges.add(IntRange(wordStart, i))
            wordStart = -1
        }

        i++
    }

    if (includeOpenWord && wordStart != -1) {
        ranges.add(IntRange(wordStart, text.length))
    }

    return ranges
}
