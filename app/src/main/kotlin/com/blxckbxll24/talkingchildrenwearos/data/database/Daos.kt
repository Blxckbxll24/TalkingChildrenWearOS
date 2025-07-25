package com.blxckbxll24.talkingchildrenwearos.data.database

import androidx.room.*
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.model.SensorData
import kotlinx.coroutines.flow.Flow

@Dao
interface HeartRateDao {
    @Query("SELECT * FROM heart_rate_data ORDER BY timestamp DESC")
    fun getAllHeartRateData(): Flow<List<HeartRateData>>

    @Query("SELECT * FROM heart_rate_data ORDER BY timestamp DESC LIMIT 1")
    fun getLatestHeartRateData(): Flow<HeartRateData?>

    @Query("SELECT * FROM heart_rate_data WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    fun getHeartRateDataSince(startTime: String): Flow<List<HeartRateData>>

    @Insert
    suspend fun insert(heartRateData: HeartRateData)

    @Update
    suspend fun update(heartRateData: HeartRateData)

    @Delete
    suspend fun delete(heartRateData: HeartRateData)

    @Query("DELETE FROM heart_rate_data WHERE timestamp < :cutoffTime")
    suspend fun deleteOldData(cutoffTime: String)
}

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity_data ORDER BY timestamp DESC")
    fun getAllActivityData(): Flow<List<ActivityData>>

    @Query("SELECT * FROM activity_data ORDER BY timestamp DESC LIMIT 1")
    fun getLatestActivityData(): Flow<ActivityData?>

    @Query("SELECT * FROM activity_data WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    fun getActivityDataSince(startTime: String): Flow<List<ActivityData>>

    @Query("SELECT SUM(steps) FROM activity_data WHERE date(timestamp) = date('now')")
    fun getTodaySteps(): Flow<Int?>

    @Insert
    suspend fun insert(activityData: ActivityData)

    @Update
    suspend fun update(activityData: ActivityData)

    @Delete
    suspend fun delete(activityData: ActivityData)

    @Query("DELETE FROM activity_data WHERE timestamp < :cutoffTime")
    suspend fun deleteOldData(cutoffTime: String)
}

@Dao
interface SensorDao {
    @Query("SELECT * FROM sensor_data ORDER BY timestamp DESC")
    fun getAllSensorData(): Flow<List<SensorData>>

    @Query("SELECT * FROM sensor_data WHERE sensorType = :type ORDER BY timestamp DESC LIMIT 1")
    fun getLatestSensorDataByType(type: String): Flow<SensorData?>

    @Insert
    suspend fun insert(sensorData: SensorData)

    @Query("DELETE FROM sensor_data WHERE timestamp < :cutoffTime")
    suspend fun deleteOldData(cutoffTime: String)
}