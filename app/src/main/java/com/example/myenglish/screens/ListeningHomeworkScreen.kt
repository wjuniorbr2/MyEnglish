package com.example.myenglish.screens

import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
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
import com.example.myenglish.components.addListeningHintIndex
import com.example.myenglish.components.openListeningHintState
import com.example.myenglish.components.selectedListeningHintCount
import com.example.myenglish.data.HomeworkSentence
import com.example.myenglish.sendHomeworkReportToTeacher
import com.example.myenglish.utils.currentDateTimeText
import com.example.myenglish.utils.hideKeyboardOnBackgroundTap
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
    val handler = remember { Handler(Looper.getMainLooper()) }
    val focus = remember(sentences.size) {
        Array(sentences.size) { FocusRequester() }
    }

    var topRequest by remember { mutableIntStateOf(0) }
    var player by remember { mutableStateOf<MediaPlayer?>(null) }
    var audioPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(topRequest) {
        if (topRequest > 0) scroll.scrollTo(0)
    }

    fun focusAnswerAfterAudio(index: Int) {
        handler.postDelayed(
            {
                if (!audioPlaying && index in focus.indices) {
                    focus[index].requestFocus()
                    keyboard?.show()
                }
            },
            140L
        )
    }

    fun stopAudio() {
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
        audioPlaying = false
    }

    fun finishRecordedAudio(index: Int, currentPlayer: MediaPlayer) {
        if (player !== currentPlayer) return

        try {
            currentPlayer.stop()
        } catch (_: Exception) {
        }

        try {
            currentPlayer.release()
        } catch (_: Exception) {
        }

        player = null
        audioPlaying = false
        focusAnswerAfterAudio(index)
    }

    fun play(index: Int, startMs: Int, endMs: Int): Boolean {
        stopAudio()

        if (index !in sentences.indices || audioResId == 0) return false

        val currentPlayer = MediaPlayer.create(context, audioResId)
            ?: return false

        player = currentPlayer
        audioPlaying = true
        focusManager.clearFocus(force = true)
        keyboard?.hide()

        currentPlayer.setVolume(1f, 1f)
        currentPlayer.seekTo(startMs)
        currentPlayer.setOnCompletionListener {
            finishRecordedAudio(index, currentPlayer)
        }
        currentPlayer.start()

        handler.postDelayed(
            {
                finishRecordedAudio(index, currentPlayer)
            },
            (endMs - startMs).coerceAtLeast(100).toLong()
        )

        return true
    }

    fun buildReport(currentScore: Int): String {
        val totalPlays = plays.sum()
        val totalHints = hints.sumOf { selectedListeningHintCount(it) }
        val builder = StringBuilder()

        builder.append("Student: ").append(studentName).append("\n")
        builder.append("Submitted at: ").append(currentDateTimeText()).append("\n")
        builder.append("Lesson: ").append(name).append("\n")
        builder.append("Homework: Listening homework\n")
        builder.append("Original score: ")
            .append(currentScore)
            .append(" / ")
            .append(sentences.size)
            .append("\n")
        builder.append("Total plays: ").append(totalPlays).append("\n")
        builder.append("Total hints: ").append(totalHints).append("\n\n")

        for (index in sentences.indices) {
            val firstTryWasCorrect =
                index in firstCorrect.indices && firstCorrect[index]

            builder.append(sentences[index].label)
                .append(": plays = ")
                .append(plays[index])
                .append(", hints = ")
                .append(selectedListeningHintCount(hints[index]))
                .append("\n")
            builder.append("Expected English: ")
                .append(sentences[index].correctText)
                .append("\n")
            builder.append("Written answer: ")
                .append(answers[index])
                .append("\n")
            builder.append("First try correct: ")
                .append(if (firstTryWasCorrect) "yes" else "no")
                .append("\n\n")
        }

        return builder.toString()
    }

    fun submitAnswers() {
        stopAudio()
        focusManager.clearFocus()

        var currentScore = 0

        for (index in sentences.indices) {
            val correct = isCorrectAnswer(
                answers[index],
                sentences[index].correctText
            )
            firstCorrect[index] = correct
            if (correct) currentScore++
        }

        setScore(currentScore)
        setStep(1)
        setMsg(
            "Pretty good! The commas are clapping. " +
                "$currentScore / ${sentences.size}"
        )
        setReport(buildReport(currentScore))
        onAttemptChanged()
        topRequest++
    }

    fun submitCorrections() {
        stopAudio()
        focusManager.clearFocus()

        val allCorrect = sentences.indices.all { index ->
            isCorrectAnswer(
                answers[index],
                sentences[index].correctText
            )
        }

        if (!allCorrect) {
            setMsg(
                "The red Xs are still causing drama. " +
                    "Mission: eliminate red Xs"
            )
            setReport(buildReport(score))
            onAttemptChanged()
            topRequest++
            return
        }

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
                    setMsg(
                        "Boom! Your masterpiece has been sent to your teacher."
                    )
                } else {
                    setMsg("Failed to send report. Please try again.")
                }
            }
        }

        topRequest++
    }

    DisposableEffect(Unit) {
        onDispose {
            stopAudio()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scroll)
            .hideKeyboardOnBackgroundTap(focusManager, keyboard)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Header(name, "Listening")

        if (msg.isNotBlank()) {
            Spacer(Modifier.height(8.dp))
            Text(
                text = msg,
                color = Color(0xFF555555),
                fontWeight = FontWeight.Bold,
                style = LocalTextStyle.current.copy(
                    shadow = Shadow(
                        Color.White,
                        Offset(1f, 1f),
                        3f
                    )
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

        for (index in sentences.indices) {
            val sentence = sentences[index]

            SentenceRow(
                sentence = sentence,
                answer = answers[index],
                change = {
                    if (!audioPlaying) {
                        answers[index] = it
                        onAttemptChanged()
                    }
                },
                playCount = plays[index],
                submitStep = submitStep,
                firstOk = firstCorrect[index],
                currentOk = isCorrectAnswer(
                    answers[index],
                    sentence.correctText
                ),
                hintCount = hints[index],
                focus = focus[index],
                inputEnabled = !audioPlaying,
                play = {
                    if (
                        play(
                            index,
                            sentence.startMs,
                            sentence.endMs
                        )
                    ) {
                        plays[index]++
                        onAttemptChanged()
                    }
                },
                stop = { stopAudio() },
                hint = { wordIndex ->
                    if (
                        answers[index].isNotBlank() &&
                        plays[index] >= 5 &&
                        submitStep >= 1 &&
                        submitStep < 2
                    ) {
                        hints[index] =
                            if (wordIndex == null) {
                                openListeningHintState(hints[index])
                            } else {
                                addListeningHintIndex(
                                    hints[index],
                                    wordIndex
                                )
                            }
                        setReport(buildReport(score))
                        onAttemptChanged()
                    }
                },
                messageIndex = index
            )

            Spacer(Modifier.height(12.dp))
        }

        when (submitStep) {
            0 -> ArtButton(
                "Correct me, I dare you!",
                { submitAnswers() }
            )

            1 -> ArtButton(
                "Submit corrections",
                { submitCorrections() }
            )

            else -> Button(
                onClick = {},
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
                stopAudio()
                back()
            },
            modifier = Modifier.fillMaxWidth(0.45f),
            backgroundResId = R.drawable.redbutton
        )
    }
}
