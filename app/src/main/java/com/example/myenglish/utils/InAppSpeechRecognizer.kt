package com.example.myenglish.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberInAppSpeechRecognizer(
    onResult: (String) -> Unit,
    onError: (String) -> Unit,
    onListeningChanged: (Boolean) -> Unit
): InAppSpeechRecognizer {
    val context = LocalContext.current
    val latestResult = rememberUpdatedState(onResult)
    val latestError = rememberUpdatedState(onError)
    val latestListeningChanged = rememberUpdatedState(onListeningChanged)

    val recognizer = remember(context) {
        InAppSpeechRecognizer(
            context = context.applicationContext,
            onResult = { latestResult.value(it) },
            onError = { latestError.value(it) },
            onListeningChanged = { latestListeningChanged.value(it) }
        )
    }

    DisposableEffect(recognizer) {
        onDispose { recognizer.destroy() }
    }

    return recognizer
}

class InAppSpeechRecognizer(
    private val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit,
    private val onListeningChanged: (Boolean) -> Unit
) {
    private var speechRecognizer: SpeechRecognizer? = null
    private var listening = false
    private var ignoreCallbacks = false
    private var destroyed = false

    fun start() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("Speech recognition is not available on this device.")
            return
        }

        ignoreCallbacks = false
        destroyed = false

        val recognizer = speechRecognizer ?: SpeechRecognizer.createSpeechRecognizer(context).also {
            speechRecognizer = it
            it.setRecognitionListener(listener)
        }

        try {
            setListening(true)
            recognizer.startListening(
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                    putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                }
            )
        } catch (_: Exception) {
            setListening(false)
            onError("Could not start the microphone. Try again.")
        }
    }

    fun cancel() {
        ignoreCallbacks = true
        try { speechRecognizer?.cancel() } catch (_: Exception) { }
        setListening(false)
    }

    fun destroy() {
        destroyed = true
        cancel()
        try { speechRecognizer?.destroy() } catch (_: Exception) { }
        speechRecognizer = null
    }

    private fun setListening(value: Boolean) {
        if (listening == value) return
        listening = value
        onListeningChanged(value)
    }

    private val listener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            setListening(true)
        }

        override fun onBeginningOfSpeech() = Unit
        override fun onRmsChanged(rmsdB: Float) = Unit
        override fun onBufferReceived(buffer: ByteArray?) = Unit
        override fun onEndOfSpeech() = Unit
        override fun onPartialResults(partialResults: Bundle?) = Unit
        override fun onEvent(eventType: Int, params: Bundle?) = Unit

        override fun onError(error: Int) {
            setListening(false)
            if (ignoreCallbacks || destroyed) {
                ignoreCallbacks = false
                return
            }
            onError(errorMessage(error))
        }

        override fun onResults(results: Bundle?) {
            setListening(false)
            if (ignoreCallbacks || destroyed) {
                ignoreCallbacks = false
                return
            }

            val spokenText = results
                ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                ?.firstOrNull()
                ?.trim()
                ?: ""

            if (spokenText.isBlank()) {
                onError("The microphone heard mysterious silence. Try again.")
            } else {
                onResult(spokenText)
            }
        }
    }
}

private fun errorMessage(error: Int): String {
    return when (error) {
        SpeechRecognizer.ERROR_AUDIO -> "The microphone had an audio problem. Try again."
        SpeechRecognizer.ERROR_CLIENT -> "The microphone stopped too early. Try again."
        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission is needed."
        SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Speech recognition needs internet right now. Try again."
        SpeechRecognizer.ERROR_NO_MATCH -> "The microphone did not catch the sentence. Try again."
        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "The microphone is already listening. Try again in a moment."
        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "The microphone heard silence. Try again."
        SpeechRecognizer.ERROR_SERVER -> "Speech recognition had a server problem. Try again."
        else -> "Speech recognition stopped. Try again."
    }
}
