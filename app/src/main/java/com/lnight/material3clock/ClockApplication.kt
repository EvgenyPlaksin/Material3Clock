package com.lnight.material3clock

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import com.lnight.material3clock.core.NotificationConstants
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class ClockApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        createAlarmNotificationChannel()
    }

    private fun createAlarmNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sound = Uri.parse("android.resource://" + packageName + "/" + R.raw.alarm_sound)
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            val channel = NotificationChannel(
                NotificationConstants.ALARM_CHANNEL_ID,
                NotificationConstants.ALARM_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Used for the alarm notifications"
            channel.setSound(sound, attributes)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}