package com.lnight.material3clock.alarm_feature.data.notification_service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.lnight.material3clock.R
import com.lnight.material3clock.alarm_feature.receivers.AlarmStopReceiver
import com.lnight.material3clock.core.ExtraKeys
import com.lnight.material3clock.core.NotificationConstants
import com.lnight.material3clock.core.presentation.MainActivity

class AlarmNotificationServiceImpl(
    private val context: Context
) : AlarmNotificationService {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showNotification(title: String, description: String?, id: Int) {
        val activityIntent = Intent(context, MainActivity::class.java)
        val activityPendingIntent = PendingIntent.getActivity(
            context,
            1,
            activityIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val cancelIntent = Intent(context, AlarmStopReceiver::class.java).apply {
            putExtra(ExtraKeys.ALARM_ID, id)
        }
        val cancelPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            cancelIntent,
            PendingIntent.FLAG_IMMUTABLE
        )
        val sound = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.alarm_sound)
        val vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
        val notificationBuilder = NotificationCompat.Builder(context, NotificationConstants.ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(activityPendingIntent)
            .setSound(sound)
            .setVibrate(vibrationPattern)
            .addAction(
                R.drawable.ic_alarm,
                "Stop",
                cancelPendingIntent
            )
        if(description != null) notificationBuilder.setContentText(description)

        val notification = notificationBuilder.build()
        notificationManager.notify(id, notification)
    }

    override fun cancelNotification(id: Int) {
        notificationManager.cancel(id)
    }
}