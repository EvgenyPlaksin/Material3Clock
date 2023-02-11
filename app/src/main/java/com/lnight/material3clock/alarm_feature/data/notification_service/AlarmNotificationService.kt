package com.lnight.material3clock.alarm_feature.data.notification_service

interface AlarmNotificationService {

    fun showNotification(title: String, description: String?, id: Int)

    fun cancelNotification()
}