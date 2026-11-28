package com.edureminder.quicktube.feature.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.edureminder.quicktube.core.designsystem.icon.QtIcons

@Composable
actual fun VoiceSearchControl(
    onResult: (String) -> Unit,
    onError: (String) -> Unit,
) {
    val context = LocalContext.current
    val reportError = onError
    var language by remember { mutableStateOf("hi-IN") }
    var menuExpanded by remember { mutableStateOf(false) }
    var recognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

    fun startRecognition(locale: String) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            reportError("Voice recognition isn't available on this device.")
            return
        }
        recognizer?.cancel()
        recognizer?.destroy()
        val speech = SpeechRecognizer.createSpeechRecognizer(context)
        recognizer = speech
        speech.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: android.os.Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {}
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
            override fun onPartialResults(partialResults: android.os.Bundle?) {}
            override fun onResults(results: android.os.Bundle?) {
                val spoken = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.trim()
                if (!spoken.isNullOrBlank()) onResult(spoken)
                else reportError("No speech was recognized. Try again or type your search.")
            }
            override fun onError(error: Int) {
                reportError("Voice search couldn't recognize that. Try again or type your search.")
            }
        })
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, locale)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, false)
        }
        runCatching { speech.startListening(intent) }
            .onFailure { reportError("Couldn't start voice recognition. Try typing your search.") }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) startRecognition(language)
        else reportError("Microphone permission was denied. You can still search by typing.")
    }

    DisposableEffect(Unit) {
        onDispose {
            recognizer?.cancel()
            recognizer?.destroy()
            recognizer = null
        }
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { menuExpanded = true }) {
            Icon(QtIcons.Mic, contentDescription = "Voice search")
        }
        DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
            DropdownMenuItem(
                text = { Text("हिंदी · speech service") },
                onClick = {
                    menuExpanded = false
                    language = "hi-IN"
                    if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        startRecognition("hi-IN")
                    } else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                },
            )
            DropdownMenuItem(
                text = { Text("English · speech service") },
                onClick = {
                    menuExpanded = false
                    language = "en-IN"
                    if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        startRecognition("en-IN")
                    } else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                },
            )
        }
    }
}
