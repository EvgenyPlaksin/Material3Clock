package com.lnight.material3clock.clock_feature.presentation.screen_saver

sealed interface BatterySaverScreenEvent {
    object OnSurfaceClick : BatterySaverScreenEvent
}