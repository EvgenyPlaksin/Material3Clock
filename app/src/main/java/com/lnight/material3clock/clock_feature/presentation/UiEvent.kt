package com.lnight.material3clock.clock_feature.presentation

sealed interface UiEvent {
    class Navigate(val route: String) : UiEvent
}