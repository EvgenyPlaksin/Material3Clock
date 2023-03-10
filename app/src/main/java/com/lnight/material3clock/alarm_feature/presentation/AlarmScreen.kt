package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.lnight.material3clock.alarm_feature.presentation.components.Alarm
import com.lnight.material3clock.alarm_feature.presentation.components.RequestNotificationsPermission
import com.lnight.material3clock.alarm_feature.presentation.components.TitleSection
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

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

    if(shouldShowChangeLabelDialog) {
        var text by remember {
            mutableStateOf(TextFieldValue(state.changeLabelData.initialText ?: ""))
        }
        val focusRequester = remember { FocusRequester() }
        AlertDialog(
            onDismissRequest = { shouldShowChangeLabelDialog = false }
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(14.dp)
                    .height(110.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onFocusChanged {
                                if (it.hasFocus) {
                                    text = text.copy(selection = TextRange(0, text.text.length))
                                }
                            }
                            .focusRequester(focusRequester),
                        label = { Text(text = "Label") }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 6.dp)
                    ) {
                        Text(
                            text = "Cancel",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { shouldShowChangeLabelDialog = false }
                                .padding(4.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "OK",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable {
                                    viewModel.onEvent(
                                        AlarmsEvent.OnLabelChange(
                                            item = state.changeLabelData.item,
                                            label = text.text
                                        )
                                    )
                                    shouldShowChangeLabelDialog = false
                                }
                                .padding(4.dp)
                        )
                    }
                    LaunchedEffect(key1 = true) {
                        focusRequester.requestFocus()
                    }
                }
            }
        }
    }

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
                        val dateTime = if(LocalTime.now().isBefore(it)) LocalDate.now() else LocalDate.now().plusDays(1)
                        val item = AlarmStateItem(
                            dateTime = LocalDateTime.of(dateTime, it),
                            label = null,
                            repeatDays = emptyList(),
                            isDetailsVisible = false,
                            isActive = true,
                            nextDay = null,
                            shouldVibrate = true
                        )
                        viewModel.onEvent(AlarmsEvent.CreateAlarm(item))
                    }
                    is TimePickerEvents.UpdateAlarmTime -> {
                        viewModel.onEvent(
                            AlarmsEvent.ChangeAlarmTime(
                                item = state.timePickerData.eventType.item,
                                newTime = LocalDateTime.of(if(LocalTime.now().isBefore(it)) LocalDate.now() else LocalDate.now().plusDays(1), it)
                            )
                        )
                    }
                }
                shouldShowTimePickerDialog = false
            }
        )
    }
    RequestNotificationsPermission()
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        TitleSection(
            titleText = "Alarm",
            titleShadow = titleShadow,
            titleSectionAlpha = titleSectionAlpha
        )
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
                val scheduledText = when {
                    item.nextDay == null && !item.isActive -> "Not scheduled"
                    item.nextDay == null && item.dateTime.dayOfWeek.name == LocalDateTime.now()
                        .plusDays(1).dayOfWeek.name -> "Tomorrow"
                    item.nextDay?.name == LocalDateTime.now()
                        .plusDays(1).dayOfWeek.name -> "Tomorrow"
                    item.isActive && item.nextDay == null -> "Today"
                    else -> item.nextDay?.name?.lowercase()?.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    } ?: "Unknown"
                }
                Alarm(
                    scheduledText = scheduledText,
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
                    },
                    onChangeVibrateClick = { viewModel.onEvent(AlarmsEvent.OnChangeVibrationClick(item)) }
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
