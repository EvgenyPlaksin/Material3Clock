package com.lnight.material3clock.alarm_feature.data.alarm_scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.recievers.AlarmReceiver
import com.lnight.material3clock.core.ExtraKeys
import java.time.Instant
import java.time.ZoneId
import java.util.*

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override fun schedule(item: AlarmItem) {
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            val dateTime = Instant.ofEpochSecond(item.timestamp).atZone(ZoneId.systemDefault())
                .toLocalDateTime()
            val day = dateTime.dayOfWeek.name.lowercase().substring(0, 3)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val time = "$day - ${dateTime.hour}:${dateTime.minute}"
            putExtra(ExtraKeys.ALARM_LABEL, item.label)
            putExtra(ExtraKeys.ALARM_TIME, time)
            putExtra(ExtraKeys.ALARM_ID, item.id)
        }
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            item.timestamp,
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    override fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, AlarmReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

}