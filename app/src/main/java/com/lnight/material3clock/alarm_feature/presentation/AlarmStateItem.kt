package com.lnight.material3clock.alarm_feature.presentation

import com.lnight.material3clock.core.Days
import java.time.LocalDateTime

data class AlarmStateItem(
    val id: Int,
    val dateTime: LocalDateTime,
    val label: String?,
    val repeatDays: List<Days>,
    val isActive: Boolean,
    val isDetailsVisible: Boolean
)
