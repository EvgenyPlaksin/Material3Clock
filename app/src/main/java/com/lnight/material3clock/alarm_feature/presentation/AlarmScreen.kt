package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AndroidAlarmScheduler
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val scheduler = AndroidAlarmScheduler(LocalContext.current)
    var alarmItem: AlarmItem? = null
    var secondsText by rememberSaveable {
        mutableStateOf("")
    }
    var message by rememberSaveable {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = secondsText,
            onValueChange = { secondsText = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Trigger alarm in seconds")
            }
        )
        OutlinedTextField(
            value = message,
            onValueChange = { message = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(text = "Message")
            }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                alarmItem = AlarmItem(
                    timestamp = LocalDateTime.now()
                        .plusSeconds(secondsText.toLongOrNull() ?: 5).atZone(ZoneId.systemDefault()).toEpochSecond(),
                    label = message.ifBlank { null },
                    repeatDays = emptyList(),
                    isActive = true
                )
                alarmItem?.let(scheduler::schedule)
                secondsText = ""
                message = ""
            }) {
                Text(text = "Schedule")
            }
            Spacer(modifier = Modifier.width(10.dp))
            Button(onClick = {
                alarmItem?.let(scheduler::cancel)
            }) {
                Text(text = "Cancel")
            }
        }
    }
}