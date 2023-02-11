package com.lnight.material3clock.alarm_feature.presentation

data class AlarmState(
    val alarmStateItems: List<AlarmStateItem> = emptyList(),
    val timePickerData: TimePickerData = TimePickerData(),
    val changeLabelData: ChangeLabelData = ChangeLabelData(),
    val formattedNextDay: String = "Today"
)
