package com.lnight.material3clock.alarm_feature.receivers

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.lnight.material3clock.alarm_feature.data.alarm_scheduler.AlarmScheduler
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationItem
import com.lnight.material3clock.alarm_feature.data.notification_service.AlarmNotificationService
import com.lnight.material3clock.alarm_feature.domain.repository.AlarmRepository
import com.lnight.material3clock.core.*
import com.marosseleng.compose.material3.datetimepickers.time.domain.noSeconds
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import java.time.LocalDateTime
import java.time.ZoneId
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
    override fun onReceive(context: Context, intent: Intent?) = goAsync(Dispatchers.IO) {
        val dateTime = LocalDateTime.now()
        val day = dateTime.dayOfWeek.name.lowercase().substring(0, 3)
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val time = "$day - ${dateTime.toLocalTime().noSeconds()}"
        val id = intent?.getIntExtra(ExtraKeys.ALARM_ID, -1) ?: -1
        val alarm = repository.getAlarmById(id)
        if (alarm?.isActive == true) {
            if (alarm.repeatDays.isEmpty() || alarm.repeatDays.contains(dateTime.dayOfWeek.name)) {
                val alarmDateTime = alarm.timestamp.toLocalDateTime()
                if (dateTime.isEqualByMinutes(alarmDateTime)) {
                    if (alarm.label != null) {
                        val item = AlarmNotificationItem(
                            title = alarm.label,
                            description = time,
                            id = id,
                            shouldVibrate = alarm.shouldVibrate
                        )
                        alarmNotificationService.showNotification(item)
                    } else {
                        val item = AlarmNotificationItem(
                            title = time,
                            description = null,
                            id = id,
                            shouldVibrate = alarm.shouldVibrate
                        )
                        alarmNotificationService.showNotification(item)
                    }

                    if (alarm.repeatDays.isEmpty()) {
                        repository.insertItem(
                            item = alarm.copy(
                                isActive = false,
                                nextDay = null
                            )
                        )
                    } else {
                        val currentDayIndex = alarm.repeatDays.indexOf(dateTime.dayOfWeek.name)
                        val nextDay = alarm.repeatDays.elementAtOrNull(currentDayIndex + 1)
                        var currentDayInWeek = -1
                        var nextDayInWeek = -1
                        Day.values().forEachIndexed { index, dayEnum ->
                            if (dateTime.dayOfWeek.name == dayEnum.name) currentDayInWeek =
                                index + 1
                            if (nextDay == dayEnum.name) nextDayInWeek = index + 1
                        }
                        val daysAmount = (nextDayInWeek - currentDayInWeek).calculateDaysAmount()

                        val timestamp =
                            dateTime.plusDays(daysAmount).atZone(ZoneId.systemDefault())
                                .toEpochSecond()
                        val item = alarm.copy(
                            timestamp = timestamp,
                            nextDay = nextDay
                        )
                        scheduler.schedule(item)
                        repository.insertItem(item)
                    }
                }
            }
        }
    }

    companion object {
        var shouldUpdateState = true
    }
}