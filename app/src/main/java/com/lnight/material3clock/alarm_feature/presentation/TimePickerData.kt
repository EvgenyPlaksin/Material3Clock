package com.lnight.material3clock.alarm_feature.presentation

import com.marosseleng.compose.material3.datetimepickers.time.domain.noSeconds
import java.time.LocalTime

data class TimePickerData(
    val initialTime: LocalTime = LocalTime.now().noSeconds(),
    val eventType: TimePickerEvents = TimePickerEvents.CreateAlarm
)

sealed interface TimePickerEvents {
    object CreateAlarm : TimePickerEvents
    class UpdateAlarmTime(val item: AlarmStateItem): TimePickerEvents
}