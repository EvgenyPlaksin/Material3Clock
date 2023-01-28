package com.lnight.material3clock.alarm_feature.data.repository

import com.lnight.material3clock.alarm_feature.data.data_source.AlarmDao
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow

class AlarmRepositoryImpl(
    private val dao: AlarmDao
) : AlarmRepository {

    override fun getAlarms(): Flow<List<AlarmItem>> {
        return dao.getAllAlarmItems()
    }

    override suspend fun insertItem(item: AlarmItem) {
        dao.insertAlarmItem(item)
    }

    override suspend fun deleteItem(item: AlarmItem) {
        dao.deleteAlarmItem(item)
    }

    override suspend fun getAlarmById(id: Int): AlarmItem? {
        return dao.getAlarmById(id)
    }
}