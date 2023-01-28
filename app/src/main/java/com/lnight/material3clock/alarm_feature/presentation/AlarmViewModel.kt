package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.domain.use_case.AlarmUseCases
import com.lnight.material3clock.core.toAlarmItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {

    var state by mutableStateOf(AlarmState())
        private set

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AlarmsEvent) {
        when (event) {
            is AlarmsEvent.ChangeAlarmRepeat -> {
                viewModelScope.launch {
                    val newItem = event.item.copy(repeatDays = event.repeatDays)
                    val newList = state.alarmStateItems.map {
                        if (it == event.item) {
                            it.copy(repeatDays = event.repeatDays)
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
                }
            }
            is AlarmsEvent.DeleteAlarmsSection -> {
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
            AlarmsEvent.OnAlarmTimeClick ->  {
                viewModelScope.launch {
                    val time = LocalTime.now()
                    _uiEvent.send(UiEvent.ShowTimePickerDialog(time))
                }
            }
            is AlarmsEvent.ToggleDetailsSection -> {
                val newList = state.alarmStateItems.map {
                    if (it == event.item) {
                        it.copy(isDetailsVisible = !it.isDetailsVisible)
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
        }
    }
}