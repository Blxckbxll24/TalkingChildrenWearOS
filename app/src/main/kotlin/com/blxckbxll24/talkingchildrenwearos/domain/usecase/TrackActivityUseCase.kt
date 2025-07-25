package com.blxckbxll24.talkingchildrenwearos.domain.usecase

import com.blxckbxll24.talkingchildrenwearos.data.database.ActivityDao
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class TrackActivityUseCase(
    private val activityDao: ActivityDao
) {
    suspend fun recordActivity(steps: Int, distance: Float, calories: Int) {
        val activityData = ActivityData(
            steps = steps,
            distance = distance,
            calories = calories,
            timestamp = LocalDateTime.now(),
            activityType = "walking" // Agregar parámetro activityType faltante
        )
        activityDao.insert(activityData)
    }
    
    fun getActivityHistory(): Flow<List<ActivityData>> {
        return activityDao.getAllActivityData()
    }
    
    fun getRecentActivity(): Flow<ActivityData?> {
        return activityDao.getLatestActivityData()
    }
}