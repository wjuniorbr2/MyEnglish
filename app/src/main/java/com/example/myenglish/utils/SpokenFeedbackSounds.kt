package com.example.myenglish.utils

import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper

object SpokenFeedbackSounds {
    private var appContext: Context? = null
    private var lastFeedbackKey = ""
    private var lastFeedbackTimeMs = 0L

    fun setContext(context: Context) {
        appContext = context.applicationContext
    }

    fun maybePlay(studentAnswer: String, correctAnswer: String, correct: Boolean) {
        if (!calledFromSpokenSpeechResult()) return
        if (studentAnswer.isBlank()) return

        val now = System.currentTimeMillis()
        val feedbackKey = cleanAnswer(studentAnswer) + "|" + cleanAnswer(correctAnswer) + "|" + correct
        if (feedbackKey == lastFeedbackKey && now - lastFeedbackTimeMs < 2500L) return

        lastFeedbackKey = feedbackKey
        lastFeedbackTimeMs = now

        Handler(Looper.getMainLooper()).postDelayed({
            playResourceSound(if (correct) "correct" else "wrong")
        }, 1000L)
    }

    private fun playResourceSound(resourceName: String) {
        val context = contextOrCurrentApplication() ?: return
        val resourceId = context.resources.getIdentifier(resourceName, "raw", context.packageName)
        if (resourceId == 0) return

        try {
            val player = MediaPlayer.create(context, resourceId) ?: return
            player.setOnCompletionListener {
                try { it.release() } catch (_: Exception) { }
            }
            player.start()
        } catch (_: Exception) {
            // Feedback sounds should never interrupt the homework flow.
        }
    }

    private fun contextOrCurrentApplication(): Context? {
        appContext?.let { return it }

        return try {
            val activityThread = Class.forName("android.app.ActivityThread")
            val currentApplication = activityThread.getMethod("currentApplication")
            (currentApplication.invoke(null) as? Context)?.applicationContext?.also { appContext = it }
        } catch (_: Exception) {
            null
        }
    }

    private fun calledFromSpokenSpeechResult(): Boolean {
        val stack = Thread.currentThread().stackTrace
        val fromSpokenHomework = stack.any { frame -> frame.className.contains("SpokenHomeworkScreen") }
        val fromSpokenPractice = stack.any { frame -> frame.className.contains("PracticeScreen") }
        val fromSpeechRecognizerResult = stack.any { frame ->
            frame.className.contains("InAppSpeechRecognizer") && frame.methodName == "onResults"
        }
        return (fromSpokenHomework || fromSpokenPractice) && fromSpeechRecognizerResult
    }
}
