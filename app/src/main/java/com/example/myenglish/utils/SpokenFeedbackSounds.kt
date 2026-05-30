package com.example.myenglish.utils

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Handler
import android.os.Looper

object SpokenFeedbackSounds {
    private var lastFeedbackKey = ""
    private var lastFeedbackTimeMs = 0L

    fun maybePlay(studentAnswer: String, correctAnswer: String, correct: Boolean) {
        if (!calledFromSpokenHomeworkSpeechResult()) return
        if (studentAnswer.isBlank()) return

        val now = System.currentTimeMillis()
        val feedbackKey = cleanAnswer(studentAnswer) + "|" + cleanAnswer(correctAnswer) + "|" + correct

        if (feedbackKey == lastFeedbackKey && now - lastFeedbackTimeMs < 2500L) return

        lastFeedbackKey = feedbackKey
        lastFeedbackTimeMs = now

        Handler(Looper.getMainLooper()).postDelayed({
            playTone(correct)
        }, 1000L)
    }

    private fun playTone(correct: Boolean) {
        try {
            val tone = if (correct) ToneGenerator.TONE_PROP_ACK else ToneGenerator.TONE_PROP_NACK
            val generator = ToneGenerator(AudioManager.STREAM_MUSIC, 70)
            generator.startTone(tone, 140)
            Handler(Looper.getMainLooper()).postDelayed({
                try { generator.release() } catch (_: Exception) { }
            }, 400L)
        } catch (_: Exception) {
            // Feedback sounds should never interrupt the homework flow.
        }
    }

    private fun calledFromSpokenHomeworkSpeechResult(): Boolean {
        val stack = Thread.currentThread().stackTrace
        val fromSpokenHomework = stack.any { frame -> frame.className.contains("SpokenHomeworkScreen") }
        val fromSpeechRecognizerResult = stack.any { frame ->
            frame.className.contains("InAppSpeechRecognizer") && frame.methodName == "onResults"
        }
        return fromSpokenHomework && fromSpeechRecognizerResult
    }
}
