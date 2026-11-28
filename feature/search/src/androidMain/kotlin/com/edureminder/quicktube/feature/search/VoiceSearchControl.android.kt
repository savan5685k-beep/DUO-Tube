package com.edureminder.quicktube.feature.search

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.edureminder.quicktube.core.designsystem.icon.QtIcons

@Composable
actual fun VoiceSearchControl(
    onResult: (String) -> Unit,
    onError: (String) -> Unit,
) {
    val context = LocalContext.current
    var listening by remember { mutableStateOf(false) }
    var recognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
    val pulse = rememberInfiniteTransition(label = "mic-pulse")
    val pulseScale by pulse.animateFloat(
        initialValue = 1f, targetValue = 1.28f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse), label = "mic-scale",
    )
    val pulseAlpha by pulse.animateFloat(
        initialValue = 0.12f, targetValue = 0.34f,
        animationSpec = infiniteRepeatable(tween(700), RepeatMode.Reverse), label = "mic-alpha",
    )

    fun stopListening() {
        recognizer?.cancel()
        recognizer?.destroy()
        recognizer = null
        listening = false
    }
    fun startRecognition() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            onError("Voice recognition isn't available on this device.")
            return
        }
        recognizer?.cancel()
        recognizer?.destroy()
        val speech = SpeechRecognizer.createSpeechRecognizer(context)
        recognizer = speech
        listening = true
        speech.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: android.os.Bundle?) { listening = true }
            override fun onBeginningOfSpeech() { listening = true }
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() { listening = false }
            override fun onEvent(eventType: Int, params: android.os.Bundle?) {}
            override fun onPartialResults(partialResults: android.os.Bundle?) {}
            override fun onResults(results: android.os.Bundle?) {
                listening = false
                val spoken = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.firstOrNull()?.trim()
                if (!spoken.isNullOrBlank()) onResult(spoken)
                else onError("No speech was recognized. Try again or type your search.")
                speech.destroy()
                if (recognizer === speech) recognizer = null
            }
            override fun onError(error: Int) {
                listening = false
                if (error != SpeechRecognizer.ERROR_CLIENT) onError("Voice search couldn't recognize that. Try again or type your search.")
                speech.destroy()
                if (recognizer === speech) recognizer = null
            }
        })
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            // Do not force Hindi or English. On supported Android 14+ services, allow automatic language detection.
            if (Build.VERSION.SDK_INT >= 34) putExtra("android.speech.extra.LANGUAGE_DETECTION_ALLOWED", true)
        }
        runCatching { speech.startListening(intent) }
            .onFailure { listening = false; onError("Couldn't start voice recognition. Try typing your search.") }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) startRecognition()
        else onError("Microphone permission was denied. You can still search by typing.")
    }
    DisposableEffect(Unit) {
        onDispose { recognizer?.cancel(); recognizer?.destroy(); recognizer = null }
    }

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(48.dp)) {
        if (listening) {
            Box(Modifier.size(34.dp).scale(pulseScale).background(Color(0x3325D366).copy(alpha = pulseAlpha), CircleShape))
            Box(Modifier.size(40.dp).scale(pulseScale).background(Color(0x2225D366), CircleShape))
        }
        IconButton(onClick = {
            if (listening) stopListening()
            else if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) startRecognition()
            else permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }) {
            Icon(QtIcons.Mic, contentDescription = if (listening) "Stop voice input" else "Start voice input",
                tint = if (listening) Color(0xFF25D366) else androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
