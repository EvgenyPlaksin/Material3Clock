package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.lnight.material3clock.alarm_feature.presentation.components.Alarm
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val state = viewModel.state
    var shouldShowTimePickerDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var shouldShowChangeLabelDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var shackBarText by rememberSaveable {
        mutableStateOf("")
    }
    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UiEvent.ShowChangeLabelDialog -> {
                    shouldShowChangeLabelDialog = true
                }
                is UiEvent.ShowSnackBar -> {
                    shackBarText = event.text
                }
                is UiEvent.ShowTimePickerDialog -> {
                    shouldShowTimePickerDialog = true
                }
            }
        }
    }
    val listState = rememberLazyListState()
    var isAlarmsOnStartPosition by remember {
        mutableStateOf(true)
    }
    LaunchedEffect(key1 = listState.canScrollBackward) {
        if (isAlarmsOnStartPosition != !listState.canScrollBackward) {
            isAlarmsOnStartPosition = !listState.canScrollBackward
        }
    }
    val titleSectionAlpha by animateFloatAsState(
        targetValue = if (isAlarmsOnStartPosition) ContentAlpha.medium else ContentAlpha.high
    )
    val titleShadow by animateDpAsState(
        targetValue = if (isAlarmsOnStartPosition) 0.dp else 10.dp
    )

    if (shouldShowTimePickerDialog) {
        TimePickerDialog(
            initialTime = state.timePickerData.initialTime,
            is24HourFormat = true,
            title = {
                Text(text = "Select time")
            },
            onDismissRequest = {
                shouldShowTimePickerDialog = false
            },
            onTimeChange = {
                when (state.timePickerData.eventType) {
                    TimePickerEvents.CreateAlarm -> {
                        val item = AlarmStateItem(
                            dateTime = LocalDateTime.of(LocalDate.now(), it),
                            label = null,
                            repeatDays = emptyList(),
                            isDetailsVisible = false,
                            isActive = true,
                            nextDay = null
                        )
                        viewModel.onEvent(AlarmsEvent.CreateAlarm(item))
                    }
                    is TimePickerEvents.UpdateAlarmTime -> {
                        viewModel.onEvent(
                            AlarmsEvent.ChangeAlarmTime(
                                item = state.timePickerData.eventType.item,
                                newTime = LocalDateTime.of(LocalDate.now(), it)
                            )
                        )
                    }
                }
                shouldShowTimePickerDialog = false
            }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .shadow(titleShadow)
                .background(MaterialTheme.colorScheme.surface)
                .zIndex(1f)
                .alpha(titleSectionAlpha)
        ) {
            Text(
                text = "Alarm",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 20.dp)
                    .align(Alignment.BottomStart)
            )
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(120.dp))
            }
            items(
                state.alarmStateItems.size,
                key = { state.alarmStateItems[it].id }
            ) { index ->
                val item = state.alarmStateItems[index]
                Alarm(
                    item = item,
                    onLabelClick = { viewModel.onEvent(AlarmsEvent.OnLabelClick(item)) },
                    onDeleteClick = { viewModel.onEvent(AlarmsEvent.OnDeleteClick(item)) },
                    onChangeTimeClick = { viewModel.onEvent(AlarmsEvent.OnAlarmTimeClick(item)) },
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
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        FloatingActionButton(
            onClick = { viewModel.onEvent(AlarmsEvent.OnAddButtonClick) },
            modifier = Modifier
                .size(105.dp)
                .align(Alignment.BottomCenter)
                .padding(bottom = 52.dp)
                .aspectRatio(1f),
            shape = CircleShape,
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Create new alarm",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}
