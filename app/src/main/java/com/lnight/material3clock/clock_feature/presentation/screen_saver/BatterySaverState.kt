package com.lnight.material3clock.clock_feature.presentation.screen_saver

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class BatterySaverState(
    val formattedTime: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE, MMM dd"))
)
