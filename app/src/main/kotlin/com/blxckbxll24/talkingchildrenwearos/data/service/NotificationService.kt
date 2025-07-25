package com.blxckbxll24.talkingchildrenwearos.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class NotificationService(private val context: Context) {
    
    companion object {
        const val SENSOR_CHANNEL_ID = "sensor_notifications"
        const val SYNC_CHANNEL_ID = "sync_notifications"
        const val SENSOR_NOTIFICATION_ID = 1001
        const val SYNC_NOTIFICATION_ID = 1002
    }
    
    private val notificationManager = NotificationManagerCompat.from(context)
    
    init {
        createNotificationChannels()
    }
    
    private fun createNotificationChannels() {
        val sensorChannel = NotificationChannel(
            SENSOR_CHANNEL_ID,
            "Sensor Monitoring",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Notifications for sensor monitoring status"
        }
        
        val syncChannel = NotificationChannel(
            SYNC_CHANNEL_ID,
            "Data Sync",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Notifications for data synchronization"
        }
        
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(sensorChannel)
        manager.createNotificationChannel(syncChannel)
    }
    
    fun showHeartRateNotification(heartRate: Int) {
        val notification = NotificationCompat.Builder(context, SENSOR_CHANNEL_ID)
            .setContentTitle("Heart Rate")
            .setContentText("Current: $heartRate BPM")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
            
        notificationManager.notify(SENSOR_NOTIFICATION_ID, notification)
    }
    
    fun showActivityNotification(steps: Int) {
        val notification = NotificationCompat.Builder(context, SENSOR_CHANNEL_ID)
            .setContentTitle("Activity")
            .setContentText("Steps: $steps")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
            
        notificationManager.notify(SENSOR_NOTIFICATION_ID + 1, notification)
    }
    
    fun showSyncNotification(message: String) {
        val notification = NotificationCompat.Builder(context, SYNC_CHANNEL_ID)
            .setContentTitle("Data Sync")
            .setContentText(message)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()
            
        notificationManager.notify(SYNC_NOTIFICATION_ID, notification)
    }
    
    fun cancelSensorNotification() {
        notificationManager.cancel(SENSOR_NOTIFICATION_ID)
    }
}