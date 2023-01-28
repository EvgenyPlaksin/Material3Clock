package com.lnight.material3clock.alarm_feature.recievers

import android.content.Context
import android.content.Intent
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationService
import com.lnight.material3clock.core.ExtraKeys
import com.lnight.material3clock.core.HiltBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmStopReceiver: HiltBroadcastReceiver() {

    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        val id = intent?.getIntExtra(ExtraKeys.ALARM_ID, -1) ?: -1
        alarmNotificationService.cancelNotification(id)
    }
}