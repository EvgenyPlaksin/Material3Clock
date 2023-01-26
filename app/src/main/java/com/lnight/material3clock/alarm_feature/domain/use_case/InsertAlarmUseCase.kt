package com.lnight.material3clock.alarm_feature.domain.use_case

import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.domain.repository.AlarmRepository

class InsertAlarmUseCase(
    private val repository: AlarmRepository
) {

    suspend operator fun invoke(item: AlarmItem) {
        repository.insertItem(item)
    }

}