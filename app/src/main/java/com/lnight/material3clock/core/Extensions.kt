package com.lnight.material3clock.core

import android.content.BroadcastReceiver
import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem
import com.lnight.material3clock.alarm_feature.presentation.AlarmStateItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.ZoneId
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