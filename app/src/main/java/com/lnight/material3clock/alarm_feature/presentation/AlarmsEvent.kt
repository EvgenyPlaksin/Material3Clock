package com.lnight.material3clock.alarm_feature.presentation

import com.lnight.material3clock.core.Day
import java.time.LocalDateTime

sealed interface AlarmsEvent {
    data class ToggleDetailsSection(val item: AlarmStateItem): AlarmsEvent
    data class OnDeleteClick(val item: AlarmStateItem): AlarmsEvent
    data class ChangeAlarmRepeat(val item: AlarmStateItem, val repeatDays: List<Day>): AlarmsEvent
    data class CreateAlarm(val item: AlarmStateItem): AlarmsEvent
    data class TurnOnOffAlarm(val item: AlarmStateItem): AlarmsEvent
    object OnAddButtonClick: AlarmsEvent
    data class OnAlarmTimeClick(val item: AlarmStateItem): AlarmsEvent
    data class OnLabelClick(val item: AlarmStateItem): AlarmsEvent
    data class OnLabelChange(val item: AlarmStateItem, val label: String): AlarmsEvent
    data class ChangeAlarmTime(val item: AlarmStateItem, val newTime: LocalDateTime): AlarmsEvent
    data class OnChangeVibrationClick(val item: AlarmStateItem): AlarmsEvent
}