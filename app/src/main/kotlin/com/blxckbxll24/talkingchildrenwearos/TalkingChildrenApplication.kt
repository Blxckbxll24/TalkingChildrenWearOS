package com.blxckbxll24.talkingchildrenwearos

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.work.Configuration
import androidx.work.WorkManager

class TalkingChildrenApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize WorkManager
        initializeWorkManager()
        
        // Create notification channels
        createNotificationChannels()
        
        // Initialize application components
        initializeApplication()
    }

    private fun initializeWorkManager() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.INFO)
            .build()
        WorkManager.initialize(this, config)
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(NotificationManager::class.java)
            
            // Sensor monitoring channel
            val sensorChannel = NotificationChannel(
                SENSOR_CHANNEL_ID,
                getString(R.string.notification_channel_sensors),
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for sensor monitoring"
                enableVibration(true)
                setShowBadge(true)
            }
            
            // Data sync channel
            val syncChannel = NotificationChannel(
                SYNC_CHANNEL_ID,
                getString(R.string.notification_channel_sync),
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Notifications for data synchronization"
                enableVibration(false)
                setShowBadge(false)
            }
            
            notificationManager.createNotificationChannel(sensorChannel)
            notificationManager.createNotificationChannel(syncChannel)
        }
    }

    private fun initializeApplication() {
        // Initialize any application-wide components here
        // This could include dependency injection setup, database initialization, etc.
    }

    companion object {
        const val SENSOR_CHANNEL_ID = "sensor_monitoring"
        const val SYNC_CHANNEL_ID = "data_sync"
    }
}