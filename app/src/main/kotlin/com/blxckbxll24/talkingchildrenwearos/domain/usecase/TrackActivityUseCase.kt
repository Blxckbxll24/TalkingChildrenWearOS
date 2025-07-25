package com.blxckbxll24.talkingchildrenwearos.domain.usecase

import com.blxckbxll24.talkingchildrenwearos.data.repository.HealthDataRepository
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

class TrackActivityUseCase(
    private val healthDataRepository: HealthDataRepository
) {
    
    suspend fun recordSteps(steps: Int): Result<Unit> {
        return try {
            val activityData = ActivityData(
                steps = steps,
                distance = calculateDistance(steps),
                calories = calculateCalories(steps),
                timestamp = LocalDateTime.now()
            )
            healthDataRepository.insertActivity(activityData)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getTodaySteps(): Int {
        return healthDataRepository.getTotalStepsForDay(LocalDateTime.now())
    }
    
    fun getActivityHistory(): Flow<List<ActivityData>> {
        return healthDataRepository.getAllActivityData()
    }
    
    suspend fun hasReachedDailyGoal(goal: Int): Boolean {
        val todaySteps = getTodaySteps()
        return todaySteps >= goal
    }
    
    private fun calculateDistance(steps: Int): Float {
        // Average stride length for children: 0.5-0.7 meters
        // Using 0.6 meters as default
        return steps * 0.0006f // kilometers
    }
    
    private fun calculateCalories(steps: Int): Int {
        // Approximate calories burned per step for children
        // Varies by weight, age, and intensity
        return (steps * 0.035f).toInt()
    }
    
    fun getActivitySummary(days: Int): ActivitySummary {
        // This would typically load from repository
        // For demo purposes, return sample data
        return ActivitySummary(
            totalSteps = 52000,
            averageStepsPerDay = 52000 / days,
            totalDistance = 26.0f,
            totalCalories = 1820,
            daysActive = days - 1
        )
    }
}

data class ActivitySummary(
    val totalSteps: Int,
    val averageStepsPerDay: Int,
    val totalDistance: Float,
    val totalCalories: Int,
    val daysActive: Int
)