package com.lnight.material3clock.core

import android.content.BroadcastReceiver
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.presentation.AlarmStateItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

fun BroadcastReceiver.goAsync(
    context: CoroutineContext = EmptyCoroutineContext,
    block: suspend CoroutineScope.() -> Unit
) {
    val pendingResult = goAsync()
    CoroutineScope(SupervisorJob()).launch(context) {
        try {
            block()
        } finally {
            pendingResult.finish()
        }
    }
}

fun AlarmStateItem.toAlarmItem(): AlarmItem {
    val newDays = repeatDays.map {
        it.name
    }
    return AlarmItem(
        id = id,
        timestamp = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond(),
        label = label,
        repeatDays = newDays,
        isActive = isActive,
        nextDay = nextDay?.name
    )
}

fun AlarmItem.toAlarmStateItem(): AlarmStateItem {
    val dateTime =  LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp),
        TimeZone.getDefault().toZoneId())
    val newDays = repeatDays.map {
        enumValueOf<Day>(it)
    }
  return AlarmStateItem(
      id = id,
      dateTime = dateTime,
      label = label,
      repeatDays = newDays,
      isActive = isActive,
      isDetailsVisible = false,
      nextDay = if(nextDay == null) null else enumValueOf<Day>(nextDay)
  )
}

fun Int.calculateDaysAmount(): Long {
    return when (this) {
        -1 -> 6
        -2 -> 5
        -3 -> 4
        -4 -> 3
        -5 -> 2
        -6 -> 1
        else -> this
    }.toLong()
}