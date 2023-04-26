package com.lnight.material3clock.clock_feature.presentation

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class ClockState(
    val formattedTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")),
    val formattedDate: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, MMM dd")),
    val nextAlarm: String? = null
)