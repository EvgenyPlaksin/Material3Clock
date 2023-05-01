package com.lnight.material3clock.clock_feature.presentation.screen_saver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnight.material3clock.alarm_feature.domain.use_case.AlarmUseCases
import com.lnight.material3clock.core.getFormattedDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BatterySaverViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(BatterySaverState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        startUpdatingTime()
        getNextAlarm()
    }

    fun onEvent(event: BatterySaverScreenEvent) {
        when (event) {
            BatterySaverScreenEvent.OnSurfaceClick -> {
                viewModelScope.launch {
                    _uiEvent.send(UiEvent.NavigateUp)
                }
            }
        }
    }

    private fun startUpdatingTime() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val currentTime = LocalDateTime.now()
                val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                _state.update {
                    it.copy(
                        formattedTime = formattedTime
                    )
                }
                delay(1000L)
            }
        }
    }

    private fun getNextAlarm() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                alarmUseCases.getAlarmsUseCase().collect{ alarms ->
                    val activeAlarms = alarms.filter { it.isActive }
                    val nextAlarm = activeAlarms.minByOrNull { it.timestamp }
                    if (nextAlarm?.isActive == true) {
                        _state.update {
                            it.copy(
                                nextAlarm = nextAlarm.getFormattedDateTime()
                            )
                        }
                    } else {
                        _state.update { it.copy(nextAlarm = null) }
                    }
                    delay(1000L)
                }
            }
        }
    }
}
