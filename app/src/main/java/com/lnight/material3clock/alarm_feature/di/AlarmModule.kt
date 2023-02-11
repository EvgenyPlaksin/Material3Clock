package com.lnight.material3clock.alarm_feature.di

import android.content.Context
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AndroidAlarmScheduler
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationService
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationServiceImpl
import com.lnight.material3clock.alarm_feature.data.repository.AlarmRepositoryImpl
import com.lnight.material3clock.alarm_feature.domain.repository.AlarmRepository
import com.lnight.material3clock.alarm_feature.domain.use_case.*
import com.lnight.material3clock.core.ClockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmScheduler(@ApplicationContext context: Context): AlarmScheduler {
        return AndroidAlarmScheduler(context)
    }

    @Provides
    @Singleton
    fun provideAlarmNotificationService(@ApplicationContext context: Context): AlarmNotificationService {
        return AlarmNotificationServiceImpl(context)
    }

    @Provides
    @Singleton
    fun provideAlarmRepository(database: ClockDatabase): AlarmRepository {
        return AlarmRepositoryImpl(database.alarmDao)
    }

    @Provides
    @Singleton
    fun provideAlarmUseCases(repository: AlarmRepository): AlarmUseCases {
        return AlarmUseCases(
            getAlarmsUseCase = GetAlarmsUseCase(repository),
            insertAlarmUseCase = InsertAlarmUseCase(repository),
            deleteAlarmUseCase = DeleteAlarmUseCase(repository),
            getAlarmByIdUseCase = GetAlarmByIdUseCase(repository)
        )
    }

}