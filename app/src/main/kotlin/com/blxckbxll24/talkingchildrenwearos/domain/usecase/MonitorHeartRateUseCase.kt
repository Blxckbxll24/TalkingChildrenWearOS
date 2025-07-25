package com.blxckbxll24.talkingchildrenwearos.domain.usecase

import com.blxckbxll24.talkingchildrenwearos.data.database.HeartRateDao
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class MonitorHeartRateUseCase(
    private val heartRateDao: HeartRateDao
) {
    suspend fun recordHeartRate(heartRate: Int) {
        val heartRateData = HeartRateData(
            heartRate = heartRate,
            timestamp = LocalDateTime.now(),
            accuracy = 1, // Agregar parámetro accuracy faltante
            isManualMeasurement = false
        )
        heartRateDao.insert(heartRateData)
    }
    
    fun getHeartRateHistory(): Flow<List<HeartRateData>> {
        return heartRateDao.getAllHeartRateData()
    }
    
    fun getRecentHeartRate(): Flow<HeartRateData?> {
        return heartRateDao.getLatestHeartRateData()
    }
}