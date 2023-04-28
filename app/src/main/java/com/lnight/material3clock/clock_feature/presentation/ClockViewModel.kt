package com.lnight.material3clock.clock_feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lnight.material3clock.alarm_feature.domain.use_case.AlarmUseCases
import com.lnight.material3clock.core.getFormattedDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(ClockState())
    val state = _state.asStateFlow()

    init {
        startUpdatingTime()
        getNextAlarm()
    }

    private fun startUpdatingTime() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val currentTime = LocalDateTime.now()
                val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                val formattedDate = currentTime.format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
                _state.update {
                    it.copy(
                        formattedTime = formattedTime,
                        formattedDate = formattedDate
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
                    val nextAlarm = alarms.minByOrNull { it.timestamp }
                    if (nextAlarm?.isActive == true) {
                        _state.update {
                            it.copy(
                                nextAlarm = nextAlarm.getFormattedDateTime()
                            )
                        }
                    } else {
                        _state.update { it.copy(nextAlarm = null) }
                    }
                    delay(5000L)
                }
            }
        }
    }
}