package com.blxckbxll24.talkingchildrenwearos.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "heart_rate_data")
data class HeartRateData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val heartRate: Int,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val accuracy: Float = 1.0f,
    val isManualMeasurement: Boolean = false
)

@Entity(tableName = "activity_data")
data class ActivityData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val steps: Int,
    val distance: Float, // in kilometers
    val calories: Int,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val activityType: String = "walking"
)

@Entity(tableName = "sensor_data")
data class SensorData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sensorType: String,
    val values: String, // JSON string of sensor values
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val accuracy: Int
)

data class UserPreferences(
    val enableNotifications: Boolean = true,
    val autoHeartRateMonitoring: Boolean = false,
    val syncWithCompanion: Boolean = true,
    val heartRateThresholdHigh: Int = 140,
    val heartRateThresholdLow: Int = 60,
    val dailyStepGoal: Int = 10000
)

data class NavigationItem(
    val id: String,
    val title: String,
    val iconResId: Int,
    val route: String
)

sealed class SensorStatus {
    object Available : SensorStatus()
    object Unavailable : SensorStatus()
    object Measuring : SensorStatus()
    object Error : SensorStatus()
}