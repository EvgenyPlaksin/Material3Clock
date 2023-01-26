package com.lnight.material3clock.alarm_feature.domain.repository

import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    fun getAlarms(): Flow<List<AlarmItem>>

    suspend fun insertItem(item: AlarmItem)

    suspend fun deleteItem(item: AlarmItem)

}