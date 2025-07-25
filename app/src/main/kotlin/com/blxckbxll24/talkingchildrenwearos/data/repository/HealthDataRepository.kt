package com.blxckbxll24.talkingchildrenwearos.data.repository

import com.blxckbxll24.talkingchildrenwearos.data.database.HeartRateDao
import com.blxckbxll24.talkingchildrenwearos.data.database.ActivityDao
import com.blxckbxll24.talkingchildrenwearos.data.database.SensorDao
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import com.blxckbxll24.talkingchildrenwearos.domain.model.SensorData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class HealthDataRepository(
    private val heartRateDao: HeartRateDao,
    private val activityDao: ActivityDao,
    private val sensorDao: SensorDao
) {
    
    // Heart Rate operations
    fun getAllHeartRateData(): Flow<List<HeartRateData>> = heartRateDao.getAllHeartRateData()
    
    fun getHeartRateDataSince(startDate: LocalDateTime): Flow<List<HeartRateData>> = 
        heartRateDao.getHeartRateDataSince(startDate)
    
    suspend fun getLatestHeartRate(): HeartRateData? = heartRateDao.getLatestHeartRate()
    
    suspend fun insertHeartRate(heartRateData: HeartRateData) = 
        heartRateDao.insertHeartRate(heartRateData)
    
    suspend fun deleteOldHeartRateData(cutoffDate: LocalDateTime) = 
        heartRateDao.deleteOldHeartRateData(cutoffDate)
    
    // Activity operations
    fun getAllActivityData(): Flow<List<ActivityData>> = activityDao.getAllActivityData()
    
    fun getActivityDataSince(startDate: LocalDateTime): Flow<List<ActivityData>> = 
        activityDao.getActivityDataSince(startDate)
    
    suspend fun getLatestActivity(): ActivityData? = activityDao.getLatestActivity()
    
    suspend fun getTotalStepsForDay(date: LocalDateTime): Int = 
        activityDao.getTotalStepsForDay(date) ?: 0
    
    suspend fun insertActivity(activityData: ActivityData) = 
        activityDao.insertActivity(activityData)
    
    suspend fun deleteOldActivityData(cutoffDate: LocalDateTime) = 
        activityDao.deleteOldActivityData(cutoffDate)
    
    // Sensor operations
    fun getSensorData(sensorType: String): Flow<List<SensorData>> = 
        sensorDao.getSensorData(sensorType)
    
    suspend fun getLatestSensorData(sensorType: String): SensorData? = 
        sensorDao.getLatestSensorData(sensorType)
    
    suspend fun insertSensorData(sensorData: SensorData) = 
        sensorDao.insertSensorData(sensorData)
    
    suspend fun deleteOldSensorData(cutoffDate: LocalDateTime) = 
        sensorDao.deleteOldSensorData(cutoffDate)
    
    // Cleanup operations
    suspend fun cleanupOldData(daysToKeep: Int = 30) {
        val cutoffDate = LocalDateTime.now().minusDays(daysToKeep.toLong())
        deleteOldHeartRateData(cutoffDate)
        deleteOldActivityData(cutoffDate)
        deleteOldSensorData(cutoffDate)
    }
}