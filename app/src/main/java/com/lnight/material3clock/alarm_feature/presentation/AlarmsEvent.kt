package com.lnight.material3clock.alarm_feature.presentation

import com.lnight.material3clock.core.Day
import java.time.LocalDateTime

sealed interface AlarmsEvent {
    class ToggleDetailsSection(val item: AlarmStateItem): AlarmsEvent
    class OnDeleteClick(val item: AlarmStateItem): AlarmsEvent
    class ChangeAlarmRepeat(val item: AlarmStateItem, val repeatDays: List<Day>): AlarmsEvent
    class CreateAlarm(val item: AlarmStateItem): AlarmsEvent
    class TurnOnOffAlarm(val item: AlarmStateItem): AlarmsEvent
    object OnAddButtonClick: AlarmsEvent
    class OnAlarmTimeClick(val item: AlarmStateItem): AlarmsEvent
    class OnLabelClick(val item: AlarmStateItem): AlarmsEvent
    class OnLabelChange(val item: AlarmStateItem, val label: String): AlarmsEvent
    class ChangeAlarmTime(val item: AlarmStateItem, val newTime: LocalDateTime): AlarmsEvent
}