package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.domain.use_case.AlarmUseCases
import com.lnight.material3clock.core.toAlarmItem
import com.lnight.material3clock.core.toAlarmStateItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    var state by mutableStateOf(AlarmState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var shouldUpdateState = true

    init {
        getAllAlarms()
    }

    fun onEvent(event: AlarmsEvent) {
        when (event) {
            is AlarmsEvent.ChangeAlarmRepeat -> {
                viewModelScope.launch {
                    val newItem = event.item.copy(
                        repeatDays = event.repeatDays,
                        nextDay = event.repeatDays.firstOrNull()
                    )
                    val newList = state.alarmStateItems.map {
                        if (it == event.item) {
                            it.copy(
                                repeatDays = event.repeatDays,
                                nextDay = event.repeatDays.firstOrNull()
                            )
                        } else it
                    }
                    state = state.copy(alarmStateItems = newList)
                    val newAlarmItem = newItem.toAlarmItem()
                    alarmUseCases.insertAlarmUseCase(newAlarmItem)
                }
            }
            is AlarmsEvent.CreateAlarm -> {
                viewModelScope.launch {
                    alarmUseCases.insertAlarmUseCase(event.item.toAlarmItem())
                    alarmScheduler.schedule(event.item.toAlarmItem())
                }
            }
            is AlarmsEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    alarmUseCases.deleteAlarmUseCase(event.item.toAlarmItem()) // TODO snackbar
                }
            }
            AlarmsEvent.OnAddButtonClick -> {
                viewModelScope.launch {
                    val time = LocalTime.now()
                    _uiEvent.send(UiEvent.ShowTimePickerDialog(time))
                }
            }
            AlarmsEvent.OnAlarmTimeClick -> {
                viewModelScope.launch {
                    val time = LocalTime.now()
                    _uiEvent.send(UiEvent.ShowTimePickerDialog(time))
                }
            }
            is AlarmsEvent.ToggleDetailsSection -> {
                val newList = state.alarmStateItems.map {
                    if (it == event.item) {
                        it.copy(isDetailsVisible = !it.isDetailsVisible)
                    } else if (!event.item.isDetailsVisible) {
                        it.copy(isDetailsVisible = false)
                    } else it
                }
                state = state.copy(alarmStateItems = newList)
            }
            is AlarmsEvent.TurnOnOffAlarm -> {
                viewModelScope.launch {
                    val newItem = event.item.copy(isActive = !event.item.isActive)
                    val newList = state.alarmStateItems.map {
                        if (it == event.item) {
                            it.copy(isActive = !it.isActive)
                        } else it
                    }
                    state = state.copy(alarmStateItems = newList)
                    val newAlarmItem = newItem.toAlarmItem()
                    alarmUseCases.insertAlarmUseCase(newAlarmItem)
                }
            }
            is AlarmsEvent.ChangeAlarmTime -> {
                viewModelScope.launch {
                    val newItem = event.item.copy(dateTime = event.newTime)
                    val newList = state.alarmStateItems.map {
                        if (it == event.item) {
                            it.copy(dateTime = event.newTime)
                        } else it
                    }
                    state = state.copy(alarmStateItems = newList)
                    val newAlarmItem = newItem.toAlarmItem()
                    alarmUseCases.insertAlarmUseCase(newAlarmItem)
                }
            }
            is AlarmsEvent.OnLabelClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.ShowChangeLabelDialog(event.item.label))
                }
            }
            is AlarmsEvent.OnLabelChange -> {
                viewModelScope.launch {
                    val newItem = event.item.copy(label = event.label.ifBlank { null })
                    val newList = state.alarmStateItems.map {
                        if (it == event.item) {
                            it.copy(label = event.label.ifBlank { null })
                        } else it
                    }
                    state = state.copy(alarmStateItems = newList)
                    val newAlarmItem = newItem.toAlarmItem()
                    alarmUseCases.insertAlarmUseCase(newAlarmItem)
                }
            }
        }
    }

    private fun getAllAlarms() {
        alarmUseCases.getAlarmsUseCase().onEach { alarms ->
            if (shouldUpdateState) {
                val alarmsState = alarms.map { it.toAlarmStateItem() }
                state = state.copy(
                    alarmStateItems = alarmsState
                )
                shouldUpdateState = false
            }
        }.launchIn(viewModelScope)
    }

}