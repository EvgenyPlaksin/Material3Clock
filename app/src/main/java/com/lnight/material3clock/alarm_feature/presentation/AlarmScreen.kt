package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.animation.core.animateDpAsState
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
import com.lnight.material3clock.alarm_feature.presentation.components.Alarm
import com.lnight.material3clock.alarm_feature.presentation.components.RequestNotificationsPermission
import com.lnight.material3clock.core_ui.TitleSection
import com.marosseleng.compose.material3.datetimepickers.time.ui.dialog.TimePickerDialog
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmScreen(
    state: AlarmState,
    uiEvent: Flow<UiEvent>,
    onEvent: (AlarmsEvent) -> Unit
) {
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
       uiEvent.collect { event ->
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
                                    onEvent(
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
                        onEvent(AlarmsEvent.CreateAlarm(item))
                    }
                    is TimePickerEvents.UpdateAlarmTime -> {
                        onEvent(
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
            titleSectionAlpha = ContentAlpha.high
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
                    onLabelClick = { onEvent(AlarmsEvent.OnLabelClick(item)) },
                    onDeleteClick = { onEvent(AlarmsEvent.OnDeleteClick(item)) },
                    onChangeTimeClick = { onEvent(AlarmsEvent.OnAlarmTimeClick(item)) },
                    onAlarmClick = { onEvent(AlarmsEvent.ToggleDetailsSection(item)) },
                    onChangeIsActive = { onEvent(AlarmsEvent.TurnOnOffAlarm(item)) },
                    onRepeatDaysChange = {
                        onEvent(
                            AlarmsEvent.ChangeAlarmRepeat(
                                item,
                                it
                            )
                        )
                    },
                    onChangeVibrateClick = { onEvent(AlarmsEvent.OnChangeVibrationClick(item)) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        FloatingActionButton(
            onClick = { onEvent(AlarmsEvent.OnAddButtonClick) },
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
