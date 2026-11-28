package com.edureminder.quicktube.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edureminder.quicktube.core.designsystem.PillButton
import com.edureminder.quicktube.core.designsystem.icon.BrandIcons
import com.edureminder.quicktube.core.designsystem.QtCard

@Composable
internal fun MoreTab(actions: HomeActions) {
    Column(
        Modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(BrandIcons.WhatsApp, null, modifier = Modifier.size(64.dp), tint = androidx.compose.ui.graphics.Color(0xFF25D366))
        Text("WhatsApp Saver", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Save WhatsApp status photos and videos you have access to, on your device.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        PillButton("Open WhatsApp Saver", onClick = actions.openStatusSaver, modifier = Modifier.fillMaxWidth())
    }
}
