package com.lnight.material3clock.alarm_feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.domain.use_case.AlarmUseCases
import com.lnight.material3clock.alarm_feature.receivers.AlarmReceiver.Companion.shouldUpdateState
import com.lnight.material3clock.core.toAlarmItem
import com.lnight.material3clock.core.toAlarmStateItem
import com.marosseleng.compose.material3.datetimepickers.time.domain.noSeconds
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
                    val newList = _state.value.alarmStateItems.map {
                        if (it == event.item) {
                            it.copy(
                                repeatDays = event.repeatDays,
                                nextDay = event.repeatDays.firstOrNull()
                            )
                        } else it
                    }
                    _state.update { it.copy(alarmStateItems = newList) }
                    val newAlarmItem = newItem.toAlarmItem()
                    alarmUseCases.insertAlarmUseCase(newAlarmItem)
                }
            }
            is AlarmsEvent.CreateAlarm -> {
                viewModelScope.launch {
                    alarmUseCases.insertAlarmUseCase(event.item.toAlarmItem())
                    shouldUpdateState = true
                }
            }
            is AlarmsEvent.OnDeleteClick -> {
                viewModelScope.launch {
                    alarmUseCases.deleteAlarmUseCase(event.item.toAlarmItem()) // TODO snackbar
                    shouldUpdateState = true
                }
            }
            AlarmsEvent.OnAddButtonClick -> {
                viewModelScope.launch {
                    val time = LocalTime.now().plusMinutes(5).noSeconds()
                    _state.update {
                        it.copy(
                            timePickerData = it.timePickerData.copy(
                                initialTime = time,
                                eventType = TimePickerEvents.CreateAlarm
                            )
                        )
                    }
                    _uiEvent.send(UiEvent.ShowTimePickerDialog)
                }
            }
            is AlarmsEvent.OnAlarmTimeClick -> {
                viewModelScope.launch {
                    val time = event.item.dateTime.toLocalTime().noSeconds()
                    _state.update {
                        it.copy(
                            timePickerData = it.timePickerData.copy(
                                initialTime = time,
                                eventType = TimePickerEvents.UpdateAlarmTime(item = event.item)
                            )
                        )
                    }
                    _uiEvent.send(UiEvent.ShowTimePickerDialog)
                }
            }
            is AlarmsEvent.ToggleDetailsSection -> {
                val newList = state.value.alarmStateItems.map {
                    if (it == event.item) {
                        it.copy(isDetailsVisible = !it.isDetailsVisible)
                    } else if (!event.item.isDetailsVisible) {
                        it.copy(isDetailsVisible = false)
                    } else it
                }
                _state.update { it.copy(alarmStateItems = newList) }
            }
            is AlarmsEvent.TurnOnOffAlarm -> {
                viewModelScope.launch {
                    val newItem = event.item.copy(isActive = !event.item.isActive)
                    val newList = state.value.alarmStateItems.map {
                        if (it == event.item) {
                            val newTime = if(it.dateTime.isBefore(LocalDateTime.now()) && !it.isActive) it.dateTime.plusDays(1) else it.dateTime
                            it.copy(
                                isActive = !it.isActive,
                                dateTime = newTime
                            )
                        } else it
                    }
                    _state.update { it.copy(alarmStateItems = newList) }
                    val newAlarmItem = newItem.toAlarmItem()
                    if(newAlarmItem.isActive) {
                        alarmScheduler.schedule(newAlarmItem)
                    }
                    alarmUseCases.insertAlarmUseCase(newAlarmItem)
                }
            }
            is AlarmsEvent.ChangeAlarmTime -> {
                viewModelScope.launch {
                    alarmUseCases.insertAlarmUseCase(event.item.copy(dateTime = event.newTime).toAlarmItem())
                    shouldUpdateState = true
                }
            }
            is AlarmsEvent.OnLabelClick -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            changeLabelData = ChangeLabelData(
                                item = event.item,
                                initialText = event.item.label
                            )
                        )
                    }
                    _uiEvent.send(UiEvent.ShowChangeLabelDialog)
                }
            }
            is AlarmsEvent.OnLabelChange -> {
                viewModelScope.launch {
                    val newItem = event.item.copy(label = event.label.ifBlank { null })
                    val newList = state.value.alarmStateItems.map {
                        if (it == event.item) {
                            it.copy(label = event.label.ifBlank { null })
                        } else it
                    }
                    _state.update { it.copy(alarmStateItems = newList) }
                    val newAlarmItem = newItem.toAlarmItem()
                    alarmUseCases.insertAlarmUseCase(newAlarmItem)
                }
            }
            is AlarmsEvent.OnChangeVibrationClick -> {
                viewModelScope.launch {
                    val newItem = event.item.copy(shouldVibrate = !event.item.shouldVibrate)
                    val newList = state.value.alarmStateItems.map {
                        if (it == event.item) {
                            it.copy(shouldVibrate = !it.shouldVibrate)
                        } else it
                    }
                    _state.update { it.copy(alarmStateItems = newList) }
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
                if(state.value.alarmStateItems.lastOrNull() != alarmsState.lastOrNull()) {
                    alarmsState.lastOrNull()?.toAlarmItem()?.let { alarmScheduler.schedule(it) }
                }
                _state.update {
                    it.copy(
                        alarmStateItems = alarmsState
                    )
                }
                shouldUpdateState = false
            }
        }.launchIn(viewModelScope)
    }

}