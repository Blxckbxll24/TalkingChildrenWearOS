package com.blxckbxll24.talkingchildrenwearos.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.blxckbxll24.talkingchildrenwearos.R
import com.blxckbxll24.talkingchildrenwearos.TalkingChildrenApplication
import com.blxckbxll24.talkingchildrenwearos.presentation.MainActivity

class NotificationService(private val context: Context) {
    
    private val notificationManager = NotificationManagerCompat.from(context)
    
    fun showHeartRateAlert(heartRate: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, TalkingChildrenApplication.SENSOR_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_heart_rate)
            .setContentTitle("Heart Rate Alert")
            .setContentText(context.getString(R.string.notification_heart_rate_high, heartRate))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_HEART_RATE, notification)
    }
    
    fun showDailyGoalAchieved(steps: Int) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        val notification = NotificationCompat.Builder(context, TalkingChildrenApplication.SENSOR_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_activity)
            .setContentTitle("Goal Achieved!")
            .setContentText(context.getString(R.string.notification_daily_goal))
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_DAILY_GOAL, notification)
    }
    
    fun showDataSyncNotification(isSuccess: Boolean) {
        val title = if (isSuccess) "Sync Complete" else "Sync Failed"
        val message = if (isSuccess) "Health data synchronized successfully" 
                     else context.getString(R.string.error_data_sync)
        
        val notification = NotificationCompat.Builder(context, TalkingChildrenApplication.SYNC_CHANNEL_ID)
            .setSmallIcon(if (isSuccess) R.drawable.ic_settings else R.drawable.ic_tile)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()
        
        notificationManager.notify(NOTIFICATION_ID_SYNC, notification)
    }
    
    fun createSensorMonitoringNotification(): android.app.Notification {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(context, TalkingChildrenApplication.SENSOR_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_heart_rate)
            .setContentTitle("Health Monitoring")
            .setContentText("Monitoring health sensors in background")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .build()
    }
    
    companion object {
        private const val NOTIFICATION_ID_HEART_RATE = 1001
        private const val NOTIFICATION_ID_DAILY_GOAL = 1002
        private const val NOTIFICATION_ID_SYNC = 1003
        const val NOTIFICATION_ID_SENSOR_SERVICE = 1000
    }
}