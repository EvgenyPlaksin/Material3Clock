package com.lnight.material3clock.alarm_feature.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmStopReceiver: BroadcastReceiver() {

    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    override fun onReceive(context: Context, intent: Intent?) {
        alarmNotificationService.cancelNotification()
    }
}