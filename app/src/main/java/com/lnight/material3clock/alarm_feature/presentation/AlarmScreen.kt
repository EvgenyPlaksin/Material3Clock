package com.lnight.material3clock.alarm_feature.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun AlarmScreen(
    viewModel: AlarmViewModel = hiltViewModel()
) {
    val state = viewModel.state

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect {event ->
            when(event) {
                is UiEvent.ShowChangeLabelDialog -> {

                }
                is UiEvent.ShowSnackBar -> {

                }
                is UiEvent.ShowTimePickerDialog -> {

                }
            }
        }
    }


}