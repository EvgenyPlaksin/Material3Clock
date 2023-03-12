package com.lnight.material3clock.alarm_feature.data.notification_service

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.lnight.material3clock.MainActivity
import com.lnight.material3clock.R
import com.lnight.material3clock.alarm_feature.receivers.AlarmStopReceiver
import com.lnight.material3clock.core.NotificationConstants
import com.lnight.material3clock.alarm_feature.receivers.AlarmReceiver.Companion.shouldUpdateState

class AlarmNotificationServiceImpl(
    private val context: Context
) : AlarmNotificationService {

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override fun showNotification(item: AlarmNotificationItem) {
        val activityIntent = Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://alarm.com/id=${item.id}"),
            context,
            MainActivity::class.java
        )
        val activityPendingIntent: PendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(activityIntent)
            getPendingIntent(
                1,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val cancelIntent = Intent(context, AlarmStopReceiver::class.java)
        val cancelPendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            cancelIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val sound = Uri.parse("android.resource://" + context.packageName + "/" + R.raw.alarm_sound)
        val vibrationPattern = longArrayOf(250, 250, 250, 250, 250)
        val notificationBuilder = NotificationCompat.Builder(context, NotificationConstants.ALARM_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_alarm)
            .setContentTitle(item.title)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(activityPendingIntent)
            .setSound(sound)
            .setFullScreenIntent(activityPendingIntent, true)
            .addAction(
                R.drawable.ic_alarm,
                "Stop",
                cancelPendingIntent
            )
        if(item.description != null) notificationBuilder.setContentText(item.description)
        if(item.shouldVibrate) notificationBuilder.setVibrate(vibrationPattern)

        val notification = notificationBuilder.build()
        notificationManager.notify(item.id, notification)
        shouldUpdateState = true
    }

    override fun cancelNotification() {
        notificationManager.cancelAll()
        shouldUpdateState = true
    }
}