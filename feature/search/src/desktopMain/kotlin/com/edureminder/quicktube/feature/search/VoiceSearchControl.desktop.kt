package com.edureminder.quicktube.feature.search

import androidx.compose.runtime.Composable

@Composable
actual fun VoiceSearchControl(
    onResult: (String) -> Unit,
    onError: (String) -> Unit,
) {
    // Voice recognition is Android-only; desktop search remains text-based.
}
