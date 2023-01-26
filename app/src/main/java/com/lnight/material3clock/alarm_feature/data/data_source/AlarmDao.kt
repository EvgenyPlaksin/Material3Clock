package com.lnight.material3clock.alarm_feature.data.data_source

import androidx.room.*
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarmitem")
    fun getAllAlarmItems(): Flow<List<AlarmItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlarmItem(item: AlarmItem)

    @Delete
    suspend fun deleteAlarmItem(item: AlarmItem)
}