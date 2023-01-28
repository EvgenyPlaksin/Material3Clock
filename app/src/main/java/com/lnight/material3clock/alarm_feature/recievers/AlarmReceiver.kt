package com.lnight.material3clock.alarm_feature.recievers

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationService
import com.lnight.material3clock.alarm_feature.domain.repository.AlarmRepository
import com.lnight.material3clock.core.Days
import com.lnight.material3clock.core.ExtraKeys
import com.lnight.material3clock.core.HiltBroadcastReceiver
import com.lnight.material3clock.core.goAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AlarmReceiver : HiltBroadcastReceiver() {

    @Inject
    lateinit var alarmNotificationService: AlarmNotificationService

    @Inject
    lateinit var scheduler: AlarmScheduler

    @Inject
    lateinit var repository: AlarmRepository

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent?) = goAsync(Dispatchers.IO) {
        super.onReceive(context, intent)

        val dateTime = LocalDateTime.now()
        val day = dateTime.dayOfWeek.name.lowercase().substring(0, 3)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val time = "$day - ${dateTime.hour}:${dateTime.minute}"
        val id = intent?.getIntExtra(ExtraKeys.ALARM_ID, -1) ?: -1
        val alarm = repository.getAlarmById(id)

        if(alarm?.label != null) {
            alarmNotificationService.showNotification(alarm.label, time, id)
        } else {
            alarmNotificationService.showNotification(time, null, id)
        }

        if(alarm?.repeatDays?.isEmpty() == true) {
            repository.insertItem(
                item = alarm.copy(isActive = false)
            )
        } else if(alarm?.repeatDays?.isEmpty() == false) {
            val currentDayIndex = alarm.repeatDays.indexOf(dateTime.dayOfWeek.name)
            val nextDay = alarm.repeatDays.elementAt(currentDayIndex + 1)
            var currentDayInWeek = -1
            var nextDayInWeek = -1
            Days.values().forEachIndexed { index, dayEnum ->
                if(dateTime.dayOfWeek.name == dayEnum.name) currentDayInWeek = index + 1
                if(nextDay == dayEnum.name) nextDayInWeek = index + 1
            }
            val daysAmount = when(nextDayInWeek - currentDayInWeek) {
                -1 -> 6
                -2 -> 5
                -3 -> 4
                -4 -> 3
                -5 -> 2
                -6 -> 1
                else -> nextDayInWeek - currentDayInWeek
            }.toLong()

            val timestamp = dateTime.plusDays(daysAmount).atZone(ZoneId.systemDefault()).toEpochSecond()
            val item = alarm.copy(timestamp = timestamp)
            scheduler.schedule(item)
            repository.insertItem(item)
        }
    }
}