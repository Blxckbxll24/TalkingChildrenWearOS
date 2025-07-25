package com.blxckbxll24.talkingchildrenwearos.data.service

import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import android.util.Log
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.DataTypeAvailability
import androidx.health.services.client.data.DataTypes
import com.blxckbxll24.talkingchildrenwearos.domain.model.HeartRateData
import com.blxckbxll24.talkingchildrenwearos.domain.model.ActivityData
import com.blxckbxll24.talkingchildrenwearos.domain.model.SensorData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.time.LocalDateTime

class SensorMonitoringService : Service(), SensorEventListener {
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + Job())
    private lateinit var sensorManager: SensorManager
    private var heartRateSensor: Sensor? = null
    private var accelerometerSensor: Sensor? = null
    private var stepCounterSensor: Sensor? = null
    
    private var isMonitoring = false
    private var stepCount = 0
    private var lastStepCount = 0
    
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "SensorMonitoringService created")
        
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        
        // Initialize sensors
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE)
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        
        // Check Health Services availability
        checkHealthServicesAvailability()
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
    
    private fun checkHealthServicesAvailability() {
        serviceScope.launch {
            try {
                val healthClient = HealthServices.getClient(this@SensorMonitoringService)
                val dataClient = healthClient.dataClient
                
                // Check heart rate availability
                val heartRateAvailability = dataClient.getCapabilities()
                Log.d(TAG, "Health Services capabilities checked")
            } catch (e: Exception) {
                Log.e(TAG, "Error checking Health Services availability", e)
            }
        }
    }
    
    private fun startMonitoring() {
        if (isMonitoring) return
        
        Log.d(TAG, "Starting sensor monitoring")
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
        
        Log.d(TAG, "Stopping sensor monitoring")
        isMonitoring = false
        
        sensorManager.unregisterListener(this)
    }
    
    private fun measureHeartRate() {
        serviceScope.launch {
            heartRateSensor?.let { sensor ->
                Log.d(TAG, "Starting heart rate measurement")
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
                        timestamp = LocalDateTime.now()
                    )
                    
                    // Here we would save to database if repository was injected
                    Log.d(TAG, "Activity data collected: $currentSteps steps")
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
                    val heartRate = sensorEvent.values[0].toInt()
                    if (heartRate > 0) {
                        serviceScope.launch {
                            val heartRateData = HeartRateData(
                                heartRate = heartRate,
                                timestamp = LocalDateTime.now(),
                                accuracy = sensorEvent.accuracy.toFloat()
                            )
                            // Here we would save to database if repository was injected
                            Log.d(TAG, "Heart rate recorded: $heartRate BPM")
                        }
                    }
                }
                
                Sensor.TYPE_STEP_COUNTER -> {
                    stepCount = sensorEvent.values[0].toInt()
                    Log.d(TAG, "Step count: $stepCount")
                }
                
                Sensor.TYPE_ACCELEROMETER -> {
                    serviceScope.launch {
                        val sensorData = SensorData(
                            sensorType = "accelerometer",
                            values = "[${sensorEvent.values.joinToString(",")}]",
                            timestamp = LocalDateTime.now(),
                            accuracy = sensorEvent.accuracy
                        )
                        // Here we would save to database if repository was injected
                        Log.d(TAG, "Accelerometer data collected")
                    }
                }
            }
        }
    }
    
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d(TAG, "Sensor accuracy changed: ${sensor?.name} - $accuracy")
    }
    
    override fun onDestroy() {
        super.onDestroy()
        stopMonitoring()
        Log.d(TAG, "SensorMonitoringService destroyed")
    }
    
    companion object {
        private const val TAG = "SensorMonitoringService"
        const val ACTION_START_MONITORING = "com.blxckbxll24.talkingchildrenwearos.START_MONITORING"
        const val ACTION_STOP_MONITORING = "com.blxckbxll24.talkingchildrenwearos.STOP_MONITORING"
        const val ACTION_MEASURE_HEART_RATE = "com.blxckbxll24.talkingchildrenwearos.MEASURE_HEART_RATE"
    }
}