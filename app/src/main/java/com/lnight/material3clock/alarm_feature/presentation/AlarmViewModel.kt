package com.lnight.material3clock.alarm_feature.presentation

import androidx.lifecycle.ViewModel
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.domain.use_case.AlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases,
    private val alarmScheduler: AlarmScheduler
): ViewModel() {



}