package com.lnight.material3clock.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.lnight.material3clock.alarm_feature.data.data_source.AlarmDao
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem

@Database(
    entities = [AlarmItem::class],
    version = 5,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class ClockDatabase: RoomDatabase() {

    abstract val alarmDao: AlarmDao

    companion object {
        const val DATABASE_NAME = "clock_db"
    }
}