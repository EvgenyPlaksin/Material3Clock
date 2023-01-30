package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.lnight.material3clock.core.Day
import java.time.LocalDateTime

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
                    val item = AlarmStateItem(
                        id = 0,
                        dateTime = LocalDateTime.now().plusSeconds(30),
                        label = "null",
                        repeatDays = listOf(Day.SUNDAY, Day.SATURDAY),
                        isActive = true,
                        isDetailsVisible = false,
                        nextDay = Day.SUNDAY
                    )
                    viewModel.onEvent(AlarmsEvent.CreateAlarm(item))
                    shouldShowTimePickerDialog = true
                }
            }
        }
    }
    val listState = rememberLazyListState()
    val isAlarmsOnStartPosition by remember {
        derivedStateOf {
            !listState.canScrollBackward
        }
    }
    val titleSectionAlpha by animateFloatAsState(
       targetValue = if(isAlarmsOnStartPosition) ContentAlpha.medium else ContentAlpha.high
    )
    val titleShadow by animateDpAsState(
        targetValue = if(isAlarmsOnStartPosition) 0.dp else 10.dp
    )

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
