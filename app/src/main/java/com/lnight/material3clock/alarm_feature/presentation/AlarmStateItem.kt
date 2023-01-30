package com.lnight.material3clock.alarm_feature.presentation

import com.lnight.material3clock.core.Day
import java.time.LocalDateTime

data class AlarmStateItem(
    val id: Int = -1,
    val dateTime: LocalDateTime,
    val label: String?,
    val repeatDays: List<Day>,
    val isActive: Boolean,
    val isDetailsVisible: Boolean,
    val nextDay: Day?
)
