package com.blxckbxll24.talkingchildrenwearos.data.repository

import com.blxckbxll24.talkingchildrenwearos.data.database.ActivityDao
import com.blxckbxll24.talkingchildrenwearos.data.database.HeartRateDao
import com.blxckbxll24.talkingchildrenwearos.data.database.SensorDao
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.model.SensorData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HealthDataRepository(
    private val heartRateDao: HeartRateDao,
    private val activityDao: ActivityDao,
    private val sensorDao: SensorDao
) {
    
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    // Heart Rate methods
    fun getHeartRateHistory(): Flow<List<HeartRateData>> = heartRateDao.getAllHeartRateData()

    fun getHeartRateDataSince(since: LocalDateTime): Flow<List<HeartRateData>> = 
        heartRateDao.getHeartRateDataSince(since.format(formatter))

    fun getLatestHeartRate(): Flow<HeartRateData?> = heartRateDao.getLatestHeartRateData()

    suspend fun insertHeartRate(heartRateData: HeartRateData) = heartRateDao.insert(heartRateData)

    suspend fun deleteOldHeartRateData(cutoffTime: LocalDateTime) = 
        heartRateDao.deleteOldData(cutoffTime.format(formatter))

    // Activity methods
    fun getActivityHistory(): Flow<List<ActivityData>> = activityDao.getAllActivityData()

    fun getActivityDataSince(since: LocalDateTime): Flow<List<ActivityData>> = 
        activityDao.getActivityDataSince(since.format(formatter))

    fun getLatestActivity(): Flow<ActivityData?> = activityDao.getLatestActivityData()

    fun getTotalStepsForDay(): Flow<Int?> = activityDao.getTodaySteps()

    suspend fun insertActivity(activityData: ActivityData) = activityDao.insert(activityData)

    suspend fun deleteOldActivityData(cutoffTime: LocalDateTime) = 
        activityDao.deleteOldData(cutoffTime.format(formatter))

    // Sensor methods
    fun getSensorData(): Flow<List<SensorData>> = sensorDao.getAllSensorData()

    fun getLatestSensorData(sensorType: String): Flow<SensorData?> = 
        sensorDao.getLatestSensorDataByType(sensorType)

    suspend fun insertSensorData(sensorData: SensorData) = sensorDao.insert(sensorData)

    suspend fun deleteOldSensorData(cutoffTime: LocalDateTime) = 
        sensorDao.deleteOldData(cutoffTime.format(formatter))
}