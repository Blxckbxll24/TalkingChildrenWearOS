package com.blxckbxll24.talkingchildrenwearos.data.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.blxckbxll24.talkingchildrenwearos.data.database.TalkingChildrenDatabase
import com.blxckbxll24.talkingchildrenwearos.data.repository.HealthDataRepository
import java.time.LocalDateTime

class DataCleanupWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = TalkingChildrenDatabase.getDatabase(applicationContext)
            val repository = HealthDataRepository(
                database.heartRateDao(),
                database.activityDao(),
                database.sensorDao()
            )
            
            // Clean up old data (older than 30 days)
            repository.cleanupOldData(30)
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

class DataSyncWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val notificationService = NotificationService(applicationContext)
            
            // Here we would implement actual sync logic with companion device
            // For demo purposes, we'll simulate a sync operation
            kotlinx.coroutines.delay(2000)
            
            val isSuccess = (0..10).random() > 2 // 80% success rate simulation
            
            notificationService.showDataSyncNotification(isSuccess)
            
            if (isSuccess) Result.success() else Result.retry()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}

class HealthMonitoringWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
    
    override suspend fun doWork(): Result {
        return try {
            val database = TalkingChildrenDatabase.getDatabase(applicationContext)
            val repository = HealthDataRepository(
                database.heartRateDao(),
                database.activityDao(),
                database.sensorDao()
            )
            val notificationService = NotificationService(applicationContext)
            
            // Check latest heart rate for abnormal values
            val latestHeartRate = repository.getLatestHeartRate()
            latestHeartRate?.let { hr ->
                if (hr.heartRate > 140 || hr.heartRate < 60) {
                    notificationService.showHeartRateAlert(hr.heartRate)
                }
            }
            
            // Check daily step goal
            val todaySteps = repository.getTotalStepsForDay(LocalDateTime.now())
            if (todaySteps >= 10000) {
                notificationService.showDailyGoalAchieved(todaySteps)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}