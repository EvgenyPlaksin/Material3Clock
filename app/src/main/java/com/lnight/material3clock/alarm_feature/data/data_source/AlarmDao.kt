package com.lnight.material3clock.alarm_feature.data.data_source

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import kotlinx.coroutines.flow.Flow

interface AlarmDao {
    @Query("SELECT * FROM alarmitem")
    fun getAllAlarmItems(): Flow<List<AlarmItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmItem(item: AlarmItem)

    @Delete
    suspend fun deleteAlarmItem(item: AlarmItem)
}