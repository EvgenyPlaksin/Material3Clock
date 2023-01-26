package com.lnight.material3clock.alarm_feature.data.alarm_scheduler

import com.lnight.material3clock.alarm_feature.domain.model.AlarmItem

interface AlarmScheduler {

    fun schedule(item: AlarmItem)

    fun cancel(item: AlarmItem)

}