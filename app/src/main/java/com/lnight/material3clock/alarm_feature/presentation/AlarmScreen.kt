package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AndroidAlarmScheduler
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.presentation.components.Alarm
import com.lnight.material3clock.core.Day
import java.time.LocalDateTime
import java.time.ZoneId

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    Box(
       modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp),
       contentAlignment = Alignment.Center
    ) {
        var item by remember {
            mutableStateOf(
            AlarmStateItem(
                id = 0,
                dateTime = LocalDateTime.now().plusDays(1),
                label = "test",
                repeatDays = listOf(Day.FRIDAY, Day.MONDAY),
                isActive = true,
                isDetailsVisible = false,
                nextDay = Day.MONDAY
            ) )
        }
        Alarm(
            item = item,
            onLabelClick = {
                           println("onLabel click")
            },
            onDeleteClick = {
                println("onDelete click")
            },
            onChangeTimeClick = {
                println("onChange click")
            },
            onAlarmClick = {
                item = item.copy(isDetailsVisible = !item.isDetailsVisible)
                println("onAlarm click")
            },
            onChangeIsActive = {
                item = item.copy(isActive = !item.isActive)
                println("onChange click")
            },
            onRepeatDaysChange = { _, _ ->
                println("onRepeatDaysChange")
            }
        )
    }
}