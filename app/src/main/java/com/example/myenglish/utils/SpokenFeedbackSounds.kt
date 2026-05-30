package com.example.myenglish.utils

import android.media.AudioManager
import android.media.ToneGenerator

object SpokenFeedbackSounds {
    private var lastFeedbackKey = ""
    private var lastFeedbackTimeMs = 0L

    fun maybePlay(studentAnswer: String, correctAnswer: String, correct: Boolean) {
        if (!calledFromSpokenHomework()) return
        if (studentAnswer.isBlank()) return

        val now = System.currentTimeMillis()
        val feedbackKey = cleanAnswer(studentAnswer) + "|" + cleanAnswer(correctAnswer) + "|" + correct

        if (feedbackKey == lastFeedbackKey && now - lastFeedbackTimeMs < 2500L) return

        lastFeedbackKey = feedbackKey
        lastFeedbackTimeMs = now

        try {
            val tone = if (correct) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_NACK
            val generator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
            generator.startTone(tone, 140)
        } catch (_: Exception) {
            // Feedback sounds should never interrupt the homework flow.
        }
    }

    private fun calledFromSpokenHomework(): Boolean {
        return Thread.currentThread().stackTrace.any { frame ->
            frame.className.contains("SpokenHomeworkScreen")
        }
    }
}
