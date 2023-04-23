package com.lnight.material3clock.core

sealed class Route(val route: String) {
    object CancelAlarmScreen : Route("cancel_alarm_screen")
}
