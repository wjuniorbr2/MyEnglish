package com.example.myenglish.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.R
import com.example.myenglish.data.HomeworkSentence
import com.example.myenglish.utils.cleanAnswer
import com.example.myenglish.utils.countWords
import com.example.myenglish.utils.revealedHintText

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
    "Feed me a word",
    "Summon tiny wisdom",
    "Unleash word goblin",
    "Bribe the sentence",
    "Give me mercy",
    "Call the clue fairy",
    "Deploy spoiler cannon",
    "Release the word",
    "Consult grammar wizard",
    "Open secret door"
)

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
    play: () -> Unit,
    stop: () -> Unit,
    hint: () -> Unit,
    modifier: Modifier = Modifier,
    messageIndex: Int = 0
) {
    var correctionEdited by remember(submitStep) { mutableStateOf(false) }

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
                    if (submitStep == 1) {
                        correctionEdited = true
                    }
                    change(it)
                },
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

                Text(attemptMessage)

                if (cleanAnswer(answer) == "") {
                    Text("Type first, beg later.")
                } else if (playCount < 5) {
                    val lockedIndex = playCount.coerceIn(0, 4)
                    Text(lockedHintMessages[lockedIndex])
                } else if (submitStep < 2 && !currentOk) {
                    val wordCount = countWords(sentence.correctText)
                    val buttonIndex = if (wordCount <= 0) 0 else (wordCount - 1) % revealButtonMessages.size

                    ArtButton(
                        text = revealButtonMessages[buttonIndex],
                        onClick = hint,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        backgroundResId = R.drawable.graybutton,
                        heightDp = 56,
                        fontSize = 16
                    )
                }

                if (hintCount > 0) {
                    Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}")
                    Text("Spoilers used: $hintCount")
                }
            }
        }
    }
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

            var i = 0
            while (i < ranges.size) {
                val range = ranges[i]
                val studentWord = cleanAnswer(rawText.substring(range.first, range.last))
                val expectedWord = if (i < correctedWords.size) correctedWords[i] else ""

                if (studentWord != "" && studentWord != expectedWord) {
                    addStyle(
                        style = SpanStyle(color = Color(0xFFC62828)),
                        start = range.first,
                        end = range.last
                    )
                }
                i++
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
