package com.lnight.material3clock.core

sealed class Route(val route: String) {
    object AlarmScreen : Route("alarm_screen")
    object CancelAlarmScreen : Route("cancel_alarm_screen")
}
