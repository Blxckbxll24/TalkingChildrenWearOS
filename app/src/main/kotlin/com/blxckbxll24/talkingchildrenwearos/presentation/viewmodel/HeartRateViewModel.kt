package com.blxckbxll24.talkingchildrenwearos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.model.SensorStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HeartRateViewModel : ViewModel() {
    
    private val _currentHeartRate = MutableStateFlow(0)
    val currentHeartRate: StateFlow<Int> = _currentHeartRate.asStateFlow()
    
    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()
    
    private val _sensorStatus = MutableStateFlow<SensorStatus>(SensorStatus.Available)
    val sensorStatus: StateFlow<SensorStatus> = _sensorStatus.asStateFlow()
    
    private val _heartRateHistory = MutableStateFlow<List<HeartRateData>>(emptyList())
    val heartRateHistory: StateFlow<List<HeartRateData>> = _heartRateHistory.asStateFlow()
    
    fun startHeartRateMonitoring() {
        viewModelScope.launch {
            _isMonitoring.value = true
            _sensorStatus.value = SensorStatus.Measuring
            // Here we would start the actual sensor monitoring
            // For demo purposes, we'll simulate heart rate data
            simulateHeartRateMeasurement()
        }
    }
    
    fun stopHeartRateMonitoring() {
        viewModelScope.launch {
            _isMonitoring.value = false
            _sensorStatus.value = SensorStatus.Available
        }
    }
    
    private suspend fun simulateHeartRateMeasurement() {
        // Simulate heart rate measurement
        kotlinx.coroutines.delay(2000)
        if (_isMonitoring.value) {
            val heartRate = (60..100).random()
            _currentHeartRate.value = heartRate
        }
    }
    
    fun loadHeartRateHistory() {
        viewModelScope.launch {
            // Here we would load from repository
            // For now, simulate some data
            val sampleData = listOf(
                HeartRateData(1, 72, java.time.LocalDateTime.now().minusHours(1)),
                HeartRateData(2, 68, java.time.LocalDateTime.now().minusHours(2)),
                HeartRateData(3, 75, java.time.LocalDateTime.now().minusHours(3))
            )
            _heartRateHistory.value = sampleData
        }
    }
}