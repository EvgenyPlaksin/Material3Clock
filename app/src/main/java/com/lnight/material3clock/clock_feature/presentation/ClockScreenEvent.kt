package com.lnight.material3clock.clock_feature.presentation

sealed interface ClockScreenEvent {
    object OnScreenSaverClick: ClockScreenEvent
}