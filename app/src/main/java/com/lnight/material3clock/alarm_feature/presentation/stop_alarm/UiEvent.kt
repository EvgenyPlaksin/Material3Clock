package com.lnight.material3clock.alarm_feature.presentation.stop_alarm

sealed interface UiEvent {
    object NavigateUp : UiEvent
    class Navigate(val route: String) : UiEvent
}