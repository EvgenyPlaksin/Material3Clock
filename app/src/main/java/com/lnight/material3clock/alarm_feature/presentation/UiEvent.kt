package com.lnight.material3clock.alarm_feature.presentation

import java.time.LocalTime

sealed interface UiEvent {
    class ShowSnackBar(val text: String): UiEvent
    class ShowTimePickerDialog(val time: LocalTime): UiEvent
}