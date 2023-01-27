package com.lnight.material3clock.alarm_feature.recievers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationService
import com.lnight.material3clock.alarm_feature.domain.repository.AlarmRepository
import com.lnight.material3clock.core.ExtraKeys
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    @Inject
    lateinit var scheduler: AlarmScheduler

    @Inject
    lateinit var repository: AlarmRepository

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent?) {
        val dateTime = LocalDateTime.now()
        val day = dateTime.dayOfWeek.name.lowercase().substring(0, 3).replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        val currentTime = "$day - ${dateTime.hour}:${dateTime.minute}"
        val message = intent?.getStringExtra(ExtraKeys.ALARM_TIME) ?: currentTime
        val label = intent?.getStringExtra(ExtraKeys.ALARM_LABEL)
        val id = intent?.getIntExtra(ExtraKeys.ALARM_ID, -1) ?: -1

        if(label != null) {
            alarmNotificationService.showNotification(label, message, id)
        } else {
            alarmNotificationService.showNotification(message, null, id)
        }
    }
}