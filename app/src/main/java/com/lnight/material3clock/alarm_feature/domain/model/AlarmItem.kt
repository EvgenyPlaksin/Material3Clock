package com.lnight.material3clock.alarm_feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class AlarmItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val timestamp: Long,
    val label: String?,
    val isRepetitive: Boolean,
    val isActive: Boolean
)