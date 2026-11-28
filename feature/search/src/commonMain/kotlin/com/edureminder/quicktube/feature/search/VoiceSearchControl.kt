package com.edureminder.quicktube.feature.search

import androidx.compose.runtime.Composable

/** Platform voice input; permission and recognition are started only after the user taps the control. */
@Composable
expect fun VoiceSearchControl(
    onResult: (String) -> Unit,
    onError: (String) -> Unit,
)
