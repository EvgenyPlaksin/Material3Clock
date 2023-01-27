package com.lnight.material3clock.alarm_feature.domain.use_case

data class AlarmUseCases(
    val getAlarmsUseCase: GetAlarmsUseCase,
    val insertAlarmUseCase: InsertAlarmUseCase,
    val deleteAlarmUseCase: DeleteAlarmUseCase
)