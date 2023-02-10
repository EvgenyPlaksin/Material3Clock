package com.lnight.material3clock.alarm_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timestamp: Long,
    val label: String?,
    val repeatDays: List<String>,
    val isActive: Boolean,
    val nextDay: String?
)