package com.lnight.material3clock.alarm_feature.presentation

import com.lnight.material3clock.core.Day
import java.time.LocalDateTime

data class AlarmStateItem(
    val id: Int = 0,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val label: String? = null,
    val repeatDays: List<Day> = emptyList(),
    val isActive: Boolean = true,
    val isDetailsVisible: Boolean = false,
    val nextDay: Day? = null,
    val shouldVibrate: Boolean = true
)
