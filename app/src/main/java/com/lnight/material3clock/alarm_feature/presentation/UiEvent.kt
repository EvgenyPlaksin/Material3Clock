package com.lnight.material3clock.alarm_feature.presentation

sealed interface UiEvent {
    class ShowSnackBar(val text: String): UiEvent
    object ShowTimePickerDialog: UiEvent
    object ShowChangeLabelDialog: UiEvent
}