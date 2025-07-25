package com.blxckbxll24.talkingchildrenwearos.data.service

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.model.SensorData
import kotlinx.coroutines.*
import java.time.LocalDateTime

class SensorMonitoringService : Service(), SensorEventListener {
    
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    private var stepCounterSensor: Sensor? = null
    
    private var isMonitoring = false
    private var stepCount = 0
    private var lastStepCount = 0
    
    override fun onCreate() {
        super.onCreate()
        
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        
        // Initialize sensors
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START_MONITORING -> startMonitoring()
            ACTION_STOP_MONITORING -> stopMonitoring()
            ACTION_MEASURE_HEART_RATE -> measureHeartRate()
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    private fun startMonitoring() {
        if (isMonitoring) return
        
        isMonitoring = true
        
        // Register sensor listeners
        heartRateSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        
        accelerometerSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        
        stepCounterSensor?.let { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
        
        // Start periodic data collection
        startPeriodicDataCollection()
    }
    
    private fun stopMonitoring() {
        if (!isMonitoring) return
        
        isMonitoring = false
        
        sensorManager.unregisterListener(this)
    }
    
    private fun measureHeartRate() {
        serviceScope.launch {
            heartRateSensor?.let { sensor ->
                // Trigger a manual heart rate measurement
                sensorManager.registerListener(this@SensorMonitoringService, sensor, SensorManager.SENSOR_DELAY_FASTEST)
                
                // Stop measurement after 30 seconds
                delay(30000)
                if (!isMonitoring) {
                    sensorManager.unregisterListener(this@SensorMonitoringService, sensor)
                }
            }
        }
    }
    
    private fun startPeriodicDataCollection() {
        serviceScope.launch {
            while (isMonitoring) {
                // Collect and save periodic activity data
                val currentSteps = stepCount - lastStepCount
                if (currentSteps > 0) {
                    val activityData = ActivityData(
                        steps = currentSteps,
                        distance = currentSteps * 0.0008f, // Rough calculation: 1 step ≈ 0.8 meters
                        calories = (currentSteps * 0.04f).toInt(), // Rough calculation: 1 step ≈ 0.04 calories
                        timestamp = LocalDateTime.now(),
                        activityType = "walking"
                    )
                    
                    // Here we would save to database if repository was injected
                    lastStepCount = stepCount
                }
                
                delay(60000) // Collect data every minute
            }
        }
    }
    
    override fun onSensorChanged(event: SensorEvent?) {
        event?.let { sensorEvent ->
            when (sensorEvent.sensor.type) {
                Sensor.TYPE_HEART_RATE -> {
                    handleHeartRateData(sensorEvent.values[0])
                }
                Sensor.TYPE_STEP_COUNTER -> {
                    handleStepData(sensorEvent.values[0].toInt())
                }
                else -> {
                    handleOtherSensorData(sensorEvent)
                }
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle accuracy changes
    }
    
    private fun handleHeartRateData(heartRate: Float) {
        val heartRateData = HeartRateData(
            heartRate = heartRate.toInt(),
            timestamp = LocalDateTime.now(),
            accuracy = 1
        )
        // TODO: Save to database
    }
    
    private fun handleStepData(steps: Int) {
        val activityData = ActivityData(
            steps = steps,
            distance = 0f,
            calories = 0,
            timestamp = LocalDateTime.now(),
            activityType = "walking"
        )
        // TODO: Save to database
    }
    
    private fun handleOtherSensorData(event: SensorEvent) {
        val sensorData = SensorData(
            sensorType = event.sensor.name,
            values = event.values.joinToString(","),
            timestamp = LocalDateTime.now(),
            accuracy = event.accuracy
        )
        // TODO: Save to database
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopMonitoring()
        serviceScope.cancel()
    }
    
    companion object {
        private const val TAG = "SensorMonitoringService"
        const val ACTION_START_MONITORING = "com.blxckbxll24.talkingchildrenwearos.START_MONITORING"
        const val ACTION_STOP_MONITORING = "com.blxckbxll24.talkingchildrenwearos.STOP_MONITORING"
        const val ACTION_MEASURE_HEART_RATE = "com.blxckbxll24.talkingchildrenwearos.MEASURE_HEART_RATE"
    }
}