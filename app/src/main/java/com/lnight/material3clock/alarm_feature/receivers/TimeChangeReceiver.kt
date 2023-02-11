package com.lnight.material3clock.alarm_feature.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.domain.use_case.AlarmUseCases
import com.lnight.material3clock.core.goAsync
import com.lnight.material3clock.core.toLocalDateTime
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@AndroidEntryPoint
class TimeChangeReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmUseCases: AlarmUseCases

    @Inject
    lateinit var scheduler: AlarmScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_TIME_CHANGED || intent.action != Intent.ACTION_TIMEZONE_CHANGED) return
        goAsync {
            alarmUseCases.getAlarmsUseCase().collect { alarms ->
                alarms.forEach {
                    val dateTime = it.timestamp.toLocalDateTime()
                    if (it.isActive && dateTime.isBefore(LocalDateTime.now())) {
                        val newItem = it.copy(
                            timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toEpochSecond()
                        )
                        alarmUseCases.insertAlarmUseCase(newItem)
                        scheduler.schedule(newItem)
                    }
                }
            }
        }
    }
}