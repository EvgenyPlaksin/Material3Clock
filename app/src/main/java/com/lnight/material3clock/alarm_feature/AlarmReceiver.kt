package com.lnight.material3clock.alarm_feature

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.lnight.material3clock.core.ExtraKeys
import java.time.LocalDateTime

class AlarmReceiver: BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context?, intent: Intent?) {
        if(context == null) return
        val currentTime = "${LocalDateTime.now().dayOfWeek.name} - ${LocalDateTime.now().hour}:${LocalDateTime.now().minute}:${LocalDateTime.now().minute}"
        val message = intent?.getStringExtra(ExtraKeys.ALARM_TIME)
        val label = intent?.getStringExtra(ExtraKeys.ALARM_LABEL) ?: message ?: currentTime
        val id = intent?.getIntExtra(ExtraKeys.ALARM_ID, -1) ?: -1

        val builder = NotificationCompat.Builder(context, "alarm_channel")
            .setSmallIcon(android.R.drawable.arrow_up_float)
            .setContentTitle(label)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        if(message != null) builder.setContentText(message)

        val pendingIntent = PendingIntent.getActivity(context, 100, intent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        builder.setFullScreenIntent(pendingIntent, true)
        with(NotificationManagerCompat.from(context)) {
            notify(id, builder.build())
        }
    }
}