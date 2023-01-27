package com.lnight.material3clock.alarm_feature.recievers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationService
import com.lnight.material3clock.core.ExtraKeys
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmStopReceiver: BroadcastReceiver() {

    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getIntExtra(ExtraKeys.ALARM_ID, -1) ?: -1
        alarmNotificationService.cancelNotification(id)
    }
}