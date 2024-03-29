package com.lnight.material3clock.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.presentation.AlarmStateItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
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
        nextDay = nextDay?.name,
        shouldVibrate = shouldVibrate
    )
}

fun AlarmItem.getFormattedDateTime(): String {
    val localDateTime = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneId.systemDefault().rules.getOffset(LocalDateTime.now()))
    val formatter = DateTimeFormatter.ofPattern("EEE HH:mm")
    return localDateTime.format(formatter)
}

fun AlarmItem.toAlarmStateItem(): AlarmStateItem {
    val dateTime = timestamp.toLocalDateTime()
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
        nextDay = if (nextDay == null) null else enumValueOf<Day>(nextDay),
        shouldVibrate = shouldVibrate
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

fun LocalDateTime.isEqualByMinutes(dateTimeToCompare: LocalDateTime) =
    this.dayOfWeek.name == dateTimeToCompare.dayOfWeek.name && this.hour == dateTimeToCompare.hour && this.minute == dateTimeToCompare.minute

val Context.activity: ComponentActivity?
    get() = when (this) {
        is ComponentActivity -> this
        is ContextWrapper -> baseContext.activity
        else -> null
    }

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofInstant(
        Instant.ofEpochSecond(this),
        TimeZone.getDefault().toZoneId()
    )
}

fun Color.dim(fraction: Float): Color {
    return lerp(this, Color(0xFF292929), fraction)
}