package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem

@Composable
fun StopAlarmScreen(
    item: AlarmItem
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = item.toString())
    }
}