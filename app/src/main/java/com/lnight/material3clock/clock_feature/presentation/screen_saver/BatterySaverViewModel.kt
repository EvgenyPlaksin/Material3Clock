package com.lnight.material3clock.clock_feature.presentation.screen_saver

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BatterySaverViewModel : ViewModel() {

    private val _state = MutableStateFlow(BatterySaverState())
    val state = _state.asStateFlow()

    init {
        startUpdatingTime()
    }

    private fun startUpdatingTime() {
        viewModelScope.launch(Dispatchers.IO) {
            while (true) {
                val currentTime = LocalDateTime.now()
                val formattedTime = currentTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                _state.update {
                    it.copy(
                        formattedTime = formattedTime
                    )
                }
                delay(1000L)
            }
        }
    }
}
