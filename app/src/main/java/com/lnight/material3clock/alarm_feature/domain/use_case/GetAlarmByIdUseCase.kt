package com.lnight.material3clock.alarm_feature.domain.use_case

import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.domain.repository.AlarmRepository

class GetAlarmByIdUseCase(
    private val repository: AlarmRepository
) {

    suspend operator fun invoke(id: Int): AlarmItem? {
        return repository.getAlarmById(id)
    }

}