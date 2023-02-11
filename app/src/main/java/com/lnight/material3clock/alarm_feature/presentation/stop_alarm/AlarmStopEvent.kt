package com.lnight.material3clock.alarm_feature.presentation.stop_alarm

import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem

sealed interface AlarmStopEvent {
    class Snooze(val item: AlarmItem): AlarmStopEvent
    class Stop(val item: AlarmItem): AlarmStopEvent
    class PassId(val id: Int): AlarmStopEvent
}