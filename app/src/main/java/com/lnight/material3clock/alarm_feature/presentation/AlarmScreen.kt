package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lnight.material3clock.alarm_feature.presentation.components.Alarm

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowChangeLabelDialog -> {

                }
                is UiEvent.ShowSnackBar -> {

                }
                is UiEvent.ShowTimePickerDialog -> {

                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .shadow(10.dp)
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Text(
                text = "Alarm",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                fontSize = 22.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 20.dp)
                    .align(Alignment.BottomStart)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            items(state.alarmStateItems) { item ->
                Alarm(
                    item = item,
                    onLabelClick = { viewModel.onEvent(AlarmsEvent.OnLabelClick(item)) },
                    onDeleteClick = { viewModel.onEvent(AlarmsEvent.OnDeleteClick(item)) },
                    onChangeTimeClick = { viewModel.onEvent(AlarmsEvent.OnAlarmTimeClick) },
                    onAlarmClick = { viewModel.onEvent(AlarmsEvent.ToggleDetailsSection(item)) },
                    onChangeIsActive = { viewModel.onEvent(AlarmsEvent.TurnOnOffAlarm(item)) },
                    onRepeatDaysChange = {
                        viewModel.onEvent(
                            AlarmsEvent.ChangeAlarmRepeat(
                                item,
                                it
                            )
                        )
                    }
                )
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    }

}