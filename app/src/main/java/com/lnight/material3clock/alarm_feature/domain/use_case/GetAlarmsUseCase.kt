package com.lnight.material3clock.alarm_feature.domain.use_case

import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class GetAlarmsUseCase(
    private val repository: AlarmRepository
) {

    operator fun invoke(): Flow<List<AlarmItem>> {
        return repository.getAlarms()
    }
}