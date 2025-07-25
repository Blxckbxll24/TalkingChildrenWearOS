package com.blxckbxll24.talkingchildrenwearos.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.usecase.MonitorHeartRateUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime

class HeartRateViewModel(
    private val monitorHeartRateUseCase: MonitorHeartRateUseCase
) : ViewModel() {

    private val _heartRateState = MutableStateFlow(HeartRateState())
    val heartRateState: StateFlow<HeartRateState> = _heartRateState.asStateFlow()

    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()

    init {
        observeHeartRateData()
    }

    private fun observeHeartRateData() {
        viewModelScope.launch {
            monitorHeartRateUseCase.getRecentHeartRate()
                .collect { heartRateData ->
                    _heartRateState.value = _heartRateState.value.copy(
                        currentHeartRate = heartRateData?.heartRate ?: 0,
                        lastMeasurement = heartRateData?.timestamp
                    )
                }
        }
    }

    fun startMonitoring() {
        _isMonitoring.value = true
        // Aquí iría la lógica para iniciar el sensor de frecuencia cardíaca
    }

    fun stopMonitoring() {
        _isMonitoring.value = false
        // Aquí iría la lógica para detener el sensor
    }

    fun recordManualHeartRate(heartRate: Int) {
        viewModelScope.launch {
            val heartRateData = HeartRateData(
                heartRate = heartRate,
                timestamp = LocalDateTime.now(),
                accuracy = 1, // Agregar accuracy
                isManualMeasurement = true
            )
            // Aquí llamarías al use case para guardar los datos
        }
    }

    fun simulateHeartRate() {
        viewModelScope.launch {
            val simulatedHR = (60..100).random()
            val heartRateData = HeartRateData(
                heartRate = simulatedHR,
                timestamp = LocalDateTime.now(),
                accuracy = 1, // Agregar accuracy
                isManualMeasurement = false
            )
            // Aquí llamarías al use case para guardar los datos
        }
    }

    fun generateDemoData() {
        viewModelScope.launch {
            val demoHR = (70..90).random()
            val heartRateData = HeartRateData(
                heartRate = demoHR,
                timestamp = LocalDateTime.now(),
                accuracy = 1, // Agregar accuracy
                isManualMeasurement = false
            )
            // Aquí llamarías al use case para guardar los datos
        }
    }
}

data class HeartRateState(
    val currentHeartRate: Int = 0,
    val lastMeasurement: LocalDateTime? = null,
    val isConnected: Boolean = false,
    val error: String? = null
)