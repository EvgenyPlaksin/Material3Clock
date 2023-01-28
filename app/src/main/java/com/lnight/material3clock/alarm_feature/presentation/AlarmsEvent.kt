package com.lnight.material3clock.alarm_feature.presentation

import com.lnight.material3clock.core.Days

sealed interface AlarmsEvent {
    class ToggleDetailsSection(val item: AlarmStateItem): AlarmsEvent
    class DeleteAlarmsSection(val item: AlarmStateItem): AlarmsEvent
    class ChangeAlarmRepeat(val item: AlarmStateItem, val repeatDays: List<Days>): AlarmsEvent
    class CreateAlarm(val item: AlarmStateItem): AlarmsEvent
    class TurnOnOffAlarm(val item: AlarmStateItem): AlarmsEvent
    object OnAddButtonClick: AlarmsEvent
    object OnAlarmTimeClick: AlarmsEvent
}