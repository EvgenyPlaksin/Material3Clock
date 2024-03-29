package com.lnight.material3clock.core

sealed class Route(val route: String) {
    object CancelAlarmScreen : Route("cancel_alarm_screen")
    object BatterySaverScreen : Route("battery_save_screen")
    object SettingsScreen : Route("settings_screen")
}
