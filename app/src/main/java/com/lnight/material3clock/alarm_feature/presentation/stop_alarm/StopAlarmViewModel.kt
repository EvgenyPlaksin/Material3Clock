package com.lnight.material3clock.alarm_feature.presentation.stop_alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.domain.use_case.AlarmUseCases
import com.lnight.material3clock.core.BottomNavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class StopAlarmViewModel @Inject constructor(
    private val scheduler: AlarmScheduler,
    private val alarmUseCases: AlarmUseCases
): ViewModel() {

    private val _state = MutableStateFlow(StopAlarmState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AlarmStopEvent) {
        when(event) {
            is AlarmStopEvent.Stop -> {
                viewModelScope.launch {
                    if(event.item.repeatDays.isNotEmpty()) {
                        _uiEvent.send(UiEvent.NavigateUp)
                        return@launch
                    }
                    launch {
                        val newItem = event.item.copy(isActive = false)
                        alarmUseCases.insertAlarmUseCase(newItem)
                    }.join()
                    _uiEvent.send(UiEvent.NavigateUp)
                }
            }
            is AlarmStopEvent.Snooze -> {
                viewModelScope.launch {
                    launch {
                        val nextTime =
                            LocalDateTime.now().plusMinutes(10).atZone(ZoneId.systemDefault())
                                .toEpochSecond()
                        val newItem = event.item.copy(
                            timestamp = nextTime,
                            isActive = true
                        )
                        alarmUseCases.insertAlarmUseCase(newItem)
                        scheduler.schedule(newItem)
                    }.join()
                    _uiEvent.send(UiEvent.NavigateUp)
                }
            }
            is AlarmStopEvent.PassId -> {
                viewModelScope.launch {
                    val item = alarmUseCases.getAlarmByIdUseCase(event.id)
                    if(item == null) {
                        _uiEvent.send(UiEvent.Navigate(BottomNavItem.Alarm.route))
                        return@launch
                    }
                    _state.update { it.copy(item = item) }
                }
            }
        }
    }
}