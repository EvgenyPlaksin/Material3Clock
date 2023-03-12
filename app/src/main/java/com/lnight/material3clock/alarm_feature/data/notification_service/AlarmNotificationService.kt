package com.lnight.material3clock.alarm_feature.data.notification_service

interface AlarmNotificationService {

    fun showNotification(item: AlarmNotificationItem)

    fun cancelNotification()
}

data class AlarmNotificationItem(
    val title: String,
    val description: String?,
    val id: Int,
    val shouldVibrate: Boolean
)