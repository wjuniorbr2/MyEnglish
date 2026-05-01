package com.example.myenglish.screens

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myenglish.R
import com.example.myenglish.components.ArtButton
import com.example.myenglish.components.Header
import com.example.myenglish.components.SentenceRow
import com.example.myenglish.data.HomeworkSentence
import com.example.myenglish.sendHomeworkReportToTeacher
import com.example.myenglish.utils.canUseHint
import com.example.myenglish.utils.currentDateTimeText
import com.example.myenglish.utils.isCorrectAnswer

@Composable
fun Homework(
    name: String,
    studentName: String,
    sentences: Array<HomeworkSentence>,
    audioResId: Int,
    answers: MutableList<String>,
    plays: MutableList<Int>,
    hints: MutableList<Int>,
    firstCorrect: MutableList<Boolean>,
    submitStep: Int,
    score: Int,
    msg: String,
    setScore: (Int) -> Unit,
    setStep: (Int) -> Unit,
    setMsg: (String) -> Unit,
    setReport: (String) -> Unit,
    onAttemptChanged: () -> Unit,
    done: () -> Unit,
    back: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    val scroll = rememberScrollState()
    var topReq by remember { mutableIntStateOf(0) }
    val handler = remember { Handler(Looper.getMainLooper()) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    val focus = remember(sentences.size) { Array(sentences.size) { FocusRequester() } }

    LaunchedEffect(scroll.isScrollInProgress) {
        if (scroll.isScrollInProgress) focusManager.clearFocus()
    }

    LaunchedEffect(topReq) {
        if (topReq > 0) scroll.scrollTo(0)
    }

    fun stop() {
        handler.removeCallbacksAndMessages(null)
        try {
            player?.stop()
        } catch (_: Exception) {
        }
        try {
            player?.release()
        } catch (_: Exception) {
        }
        player = null
    }

    fun play(i: Int, startMs: Int, endMs: Int) {
        stop()

        if (audioResId == 0) return

        val currentPlayer = MediaPlayer.create(context, audioResId) ?: return
        player = currentPlayer
        currentPlayer.setVolume(1f, 1f)
        currentPlayer.seekTo(startMs)
        currentPlayer.start()

        handler.postDelayed(
            {
                if (player == currentPlayer) {
                    stop()
                    focus[i].requestFocus()
                    keyboard?.show()
                }
            },
            (endMs - startMs).toLong()
        )
    }

    fun buildReport(currentScore: Int): String {
        val builder = StringBuilder()
        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Submitted at: ").append(currentDateTimeText()).append("\n")
        builder.append("Lesson: ").append(name).append("\n")
        builder.append("Homework: Listening homework\n")
        builder.append("Original score: ").append(currentScore).append(" / ").append(sentences.size).append("\n")

        var i = 0
        while (i < sentences.size) {
            builder.append(sentences[i].label)
            builder.append(": plays = ")
            builder.append(plays[i])
            builder.append(", hints = ")
            builder.append(hints[i])
            builder.append("\n")
            i++
        }

        return builder.toString()
    }

    fun submitAnswers() {
        stop()
        focusManager.clearFocus()

        var currentScore = 0
        var i = 0

        while (i < sentences.size) {
            val ok = isCorrectAnswer(answers[i], sentences[i].correctText)
            firstCorrect[i] = ok
            if (ok) currentScore++
            i++
        }

        setScore(currentScore)
        setStep(1)
        setMsg("Pretty good! The commas are clapping. $currentScore / ${sentences.size}")
        setReport(buildReport(currentScore))
        onAttemptChanged()
        topReq++
    }

    fun submitCorrections() {
        stop()
        focusManager.clearFocus()

        var allCorrect = true
        var i = 0

        while (i < sentences.size) {
            if (!isCorrectAnswer(answers[i], sentences[i].correctText)) allCorrect = false
            i++
        }

        if (allCorrect) {
            val finalReport = buildReport(score)

            setMsg("Delivering your masterpiece...")
            setReport(finalReport)

            sendHomeworkReportToTeacher(
                studentName = studentName,
                lessonName = name,
                homeworkType = "Listening homework",
                scoreText = "$score / ${sentences.size}",
                report = finalReport
            ) { success ->
                Handler(Looper.getMainLooper()).post {
                    if (success) {
                        setStep(2)
                        done()
                        setMsg("Boom! Your report has landed.")
                    } else {
                        setMsg("Failed to send report. Please try again.")
                    }
                }
            }
        } else {
            setMsg("The red Xs are still causing drama. Mission: eliminate red Xs")
            setReport(buildReport(score))
            onAttemptChanged()
        }

        topReq++
    }

    DisposableEffect(Unit) {
        onDispose { stop() }
    }

    Column(
        Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name, "Listening")

        if (msg != "") {
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = Color(0xFF555555),
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(Color.White, Offset(1f, 1f), 3f)
                )
            )
        }

        if (audioResId == 0) {
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Audio file not found in res/raw.",
                color = Color(0xFF555555),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.height(16.dp))

        var i = 0
        while (i < sentences.size) {
            val index = i
            val sentence = sentences[index]

            SentenceRow(
                sentence = sentence,
                answer = answers[index],
                change = {
                    answers[index] = it
                    onAttemptChanged()
                },
                playCount = plays[index],
                submitStep = submitStep,
                firstOk = firstCorrect[index],
                currentOk = isCorrectAnswer(answers[index], sentence.correctText),
                hintCount = hints[index],
                focus = focus[index],
                play = {
                    plays[index]++
                    onAttemptChanged()
                    play(index, sentence.startMs, sentence.endMs)
                },
                stop = {
                    stop()
                },
                hint = {
                    if (canUseHint(answers[index], plays[index], submitStep >= 1, hints[index], sentence.correctText)) {
                        hints[index]++
                        setReport(buildReport(score))
                        onAttemptChanged()
                    }
                },
                messageIndex = index
            )

            Spacer(Modifier.height(12.dp))
            i++
        }

        if (submitStep == 0) {
            ArtButton("Correct me, I dare you!", { submitAnswers() })
        } else if (submitStep == 1) {
            ArtButton("Submit corrections", { submitCorrections() })
        } else {
            Button(
                onClick = { },
                enabled = false,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFF2E7D32),
                    disabledContentColor = Color.White
                )
            ) {
                Text("Submitted")
            }
        }

        Spacer(Modifier.height(16.dp))

        ArtButton(
            text = "Back",
            onClick = {
                stop()
                back()
            },
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}
