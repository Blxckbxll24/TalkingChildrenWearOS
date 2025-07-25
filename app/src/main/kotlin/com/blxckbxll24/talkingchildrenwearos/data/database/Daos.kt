package com.blxckbxll24.talkingchildrenwearos.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import com.blxckbxll24.talkingchildrenwearos.domain.model.SensorData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface HeartRateDao {
    
    @Query("SELECT * FROM heart_rate_data ORDER BY timestamp DESC")
    fun getAllHeartRateData(): Flow<List<HeartRateData>>
    
    @Query("SELECT * FROM heart_rate_data WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getHeartRateDataSince(startDate: LocalDateTime): Flow<List<HeartRateData>>
    
    @Query("SELECT * FROM heart_rate_data ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestHeartRate(): HeartRateData?
    
    @Insert
    suspend fun insertHeartRate(heartRateData: HeartRateData)
    
    @Update
    suspend fun updateHeartRate(heartRateData: HeartRateData)
    
    @Delete
    suspend fun deleteHeartRate(heartRateData: HeartRateData)
    
    @Query("DELETE FROM heart_rate_data WHERE timestamp < :cutoffDate")
    suspend fun deleteOldHeartRateData(cutoffDate: LocalDateTime)
}

@Dao
interface ActivityDao {
    
    @Query("SELECT * FROM activity_data ORDER BY timestamp DESC")
    fun getAllActivityData(): Flow<List<ActivityData>>
    
    @Query("SELECT * FROM activity_data WHERE timestamp >= :startDate ORDER BY timestamp DESC")
    fun getActivityDataSince(startDate: LocalDateTime): Flow<List<ActivityData>>
    
    @Query("SELECT * FROM activity_data ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestActivity(): ActivityData?
    
    @Query("SELECT SUM(steps) FROM activity_data WHERE DATE(timestamp) = DATE(:date)")
    suspend fun getTotalStepsForDay(date: LocalDateTime): Int?
    
    @Insert
    suspend fun insertActivity(activityData: ActivityData)
    
    @Update
    suspend fun updateActivity(activityData: ActivityData)
    
    @Delete
    suspend fun deleteActivity(activityData: ActivityData)
    
    @Query("DELETE FROM activity_data WHERE timestamp < :cutoffDate")
    suspend fun deleteOldActivityData(cutoffDate: LocalDateTime)
}

@Dao
interface SensorDao {
    
    @Query("SELECT * FROM sensor_data WHERE sensorType = :sensorType ORDER BY timestamp DESC")
    fun getSensorData(sensorType: String): Flow<List<SensorData>>
    
    @Query("SELECT * FROM sensor_data WHERE sensorType = :sensorType ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestSensorData(sensorType: String): SensorData?
    
    @Insert
    suspend fun insertSensorData(sensorData: SensorData)
    
    @Query("DELETE FROM sensor_data WHERE timestamp < :cutoffDate")
    suspend fun deleteOldSensorData(cutoffDate: LocalDateTime)
}