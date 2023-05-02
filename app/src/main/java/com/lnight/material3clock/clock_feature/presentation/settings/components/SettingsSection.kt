package com.lnight.material3clock.clock_feature.presentation.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun SettingsSection(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    textSize: TextUnit = 12.sp,
    text: String
) {
    Text(
        text = text,
        fontSize = textSize,
        modifier = modifier.fillMaxWidth(),
        color = color,
        textAlign = TextAlign.Start
    )
}