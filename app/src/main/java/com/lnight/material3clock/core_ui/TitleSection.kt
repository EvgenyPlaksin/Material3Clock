package com.lnight.material3clock.core_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun TitleSection(
    titleText: String,
    modifier: Modifier = Modifier,
    titleShadow: Dp = 0.dp,
    titleSectionAlpha: Float = ContentAlpha.medium
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(110.dp)
            .shadow(titleShadow)
            .background(MaterialTheme.colorScheme.surface)
            .zIndex(1f)
            .alpha(titleSectionAlpha)
    ) {
        Text(
            text = titleText,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, bottom = 20.dp)
                .align(Alignment.BottomStart)
        )
    }
}