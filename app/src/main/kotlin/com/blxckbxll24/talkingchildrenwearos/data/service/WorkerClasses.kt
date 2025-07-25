package com.blxckbxll24.talkingchildrenwearos.data.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.ListenableWorker.Result
import com.blxckbxll24.talkingchildrenwearos.data.database.TalkingChildrenDatabase
import com.blxckbxll24.talkingchildrenwearos.data.repository.HealthDataRepository
import kotlinx.coroutines.flow.first
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
            val cutoffTime = LocalDateTime.now().minusDays(30)
            repository.deleteOldHeartRateData(cutoffTime)
            repository.deleteOldActivityData(cutoffTime)
            repository.deleteOldSensorData(cutoffTime)
            
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
            val database = TalkingChildrenDatabase.getDatabase(applicationContext)
            
            // Lógica básica de sincronización
            Result.success()
        } catch (exception: Exception) {
            Result.failure()
        }
    }
}

class HeartRateWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val database = TalkingChildrenDatabase.getDatabase(applicationContext)
            
            // Lógica básica para procesar datos de frecuencia cardíaca
            Result.success()
        } catch (exception: Exception) {
            Result.retry()
        }
    }
}

class ActivityWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val database = TalkingChildrenDatabase.getDatabase(applicationContext)
            
            // Lógica básica para procesar datos de actividad
            Result.success()
        } catch (exception: Exception) {
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
            val latestHeartRate = repository.getLatestHeartRate().first()
            latestHeartRate?.let { hr ->
                if (hr.heartRate > 140 || hr.heartRate < 60) {
                    notificationService.showHeartRateNotification(hr.heartRate)
                }
            }
            
            // Check daily step goal
            val todaySteps = repository.getTotalStepsForDay().first() ?: 0
            if (todaySteps >= 10000) {
                notificationService.showActivityNotification(todaySteps)
            }
            
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}