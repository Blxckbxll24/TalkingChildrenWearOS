package com.blxckbxll24.talkingchildrenwearos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ActivityViewModel : ViewModel() {
    
    private val _currentSteps = MutableStateFlow(0)
    val currentSteps: StateFlow<Int> = _currentSteps.asStateFlow()
    
    private val _currentDistance = MutableStateFlow(0f)
    val currentDistance: StateFlow<Float> = _currentDistance.asStateFlow()
    
    private val _currentCalories = MutableStateFlow(0)
    val currentCalories: StateFlow<Int> = _currentCalories.asStateFlow()
    
    private val _dailyGoal = MutableStateFlow(10000)
    val dailyGoal: StateFlow<Int> = _dailyGoal.asStateFlow()
    
    private val _isTracking = MutableStateFlow(false)
    val isTracking: StateFlow<Boolean> = _isTracking.asStateFlow()
    
    private val _weeklyStats = MutableStateFlow(WeeklyStats())
    val weeklyStats: StateFlow<WeeklyStats> = _weeklyStats.asStateFlow()
    
    init {
        loadTodayActivity()
        loadWeeklyStats()
    }
    
    fun startActivityTracking() {
        viewModelScope.launch {
            _isTracking.value = true
            // Here we would start the sensor monitoring service
        }
    }
    
    fun stopActivityTracking() {
        viewModelScope.launch {
            _isTracking.value = false
            // Here we would stop the sensor monitoring service
        }
    }
    
    private fun loadTodayActivity() {
        viewModelScope.launch {
            // Here we would load from repository
            // For demo purposes, simulate data
            val steps = (5000..15000).random()
            _currentSteps.value = steps
            _currentDistance.value = steps * 0.0008f
            _currentCalories.value = (steps * 0.04f).toInt()
        }
    }
    
    private fun loadWeeklyStats() {
        viewModelScope.launch {
            // Here we would load from repository
            // For demo purposes, simulate data
            _weeklyStats.value = WeeklyStats(
                averageSteps = 8500,
                goalsMet = 5,
                totalDays = 7,
                bestDay = 12450
            )
        }
    }
    
    fun updateDailyGoal(newGoal: Int) {
        viewModelScope.launch {
            _dailyGoal.value = newGoal
            // Here we would save to repository
        }
    }
    
    fun getDailyProgress(): Float {
        val steps = _currentSteps.value
        val goal = _dailyGoal.value
        return if (goal > 0) (steps.toFloat() / goal).coerceAtMost(1f) else 0f
    }
}

data class WeeklyStats(
    val averageSteps: Int = 0,
    val goalsMet: Int = 0,
    val totalDays: Int = 7,
    val bestDay: Int = 0
)