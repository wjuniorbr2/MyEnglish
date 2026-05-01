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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myenglish.R
import com.example.myenglish.data.HomeworkSentence
import com.example.myenglish.utils.cleanAnswer
import com.example.myenglish.utils.revealedHintText

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
    hint: () -> Unit
) {
    Card(
        Modifier.fillMaxWidth(),
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
                onValueChange = change,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focus)
            )

            Spacer(Modifier.height(4.dp))

            Text("Plays: $playCount")

            if (submitStep >= 1) {
                Text(if (firstOk) "First attempt: correct" else "First attempt: incorrect")

                if (cleanAnswer(answer) == "") {
                    Text("Type an answer before using hints.")
                } else if (playCount < 5) {
                    Text("Hint locked: listen ${5 - playCount} more time(s).")
                } else if (submitStep < 2 && !currentOk) {
                    ArtButton(
                        text = "Reveal next word",
                        onClick = hint,
                        modifier = Modifier.fillMaxWidth(0.7f),
                        backgroundResId = R.drawable.graybutton,
                        heightDp = 56,
                        fontSize = 16
                    )
                }

                if (hintCount > 0) {
                    Text("Hint: ${revealedHintText(sentence.correctText, hintCount)}")
                    Text("Hints used: $hintCount")
                }
            }
        }
    }
}
