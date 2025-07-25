package com.blxckbxll24.talkingchildrenwearos.domain.usecase

import com.blxckbxll24.talkingchildrenwearos.data.repository.HealthDataRepository
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class MonitorHeartRateUseCase(
    private val healthDataRepository: HealthDataRepository
) {
    
    suspend fun recordHeartRate(heartRate: Int, isManual: Boolean = false): Result<Unit> {
        return try {
            val heartRateData = HeartRateData(
                heartRate = heartRate,
                timestamp = LocalDateTime.now(),
                isManualMeasurement = isManual
            )
            healthDataRepository.insertHeartRate(heartRateData)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getHeartRateHistory(): Flow<List<HeartRateData>> {
        return healthDataRepository.getAllHeartRateData()
    }
    
    suspend fun getLatestHeartRate(): HeartRateData? {
        return healthDataRepository.getLatestHeartRate()
    }
    
    fun isHeartRateAbnormal(heartRate: Int, ageGroup: AgeGroup = AgeGroup.CHILD): Boolean {
        return when (ageGroup) {
            AgeGroup.CHILD -> heartRate < 70 || heartRate > 130
            AgeGroup.TEEN -> heartRate < 60 || heartRate > 120
            AgeGroup.ADULT -> heartRate < 60 || heartRate > 100
        }
    }
    
    enum class AgeGroup {
        CHILD, TEEN, ADULT
    }
}