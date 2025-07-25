package com.blxckbxll24.talkingchildrenwearos.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.wear.compose.material.MaterialTheme
import com.blxckbxll24.talkingchildrenwearos.presentation.composable.WearAppNavigation
import com.blxckbxll24.talkingchildrenwearos.presentation.theme.TalkingChildrenWearOSTheme

class MainActivity : ComponentActivity() {
    
    private var hasHeartRatePermission by mutableStateOf(false)
    private var hasActivityPermission by mutableStateOf(false)
    private var hasBodySensorsPermission by mutableStateOf(false)
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasHeartRatePermission = permissions[Manifest.permission.BODY_SENSORS] == true
        hasActivityPermission = permissions[Manifest.permission.ACTIVITY_RECOGNITION] == true
        hasBodySensorsPermission = permissions[Manifest.permission.BODY_SENSORS] == true
        
        Log.d(TAG, "Permissions granted: HR=$hasHeartRatePermission, Activity=$hasActivityPermission, BodySensors=$hasBodySensorsPermission")
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        checkPermissions()
        requestPermissionsIfNeeded()
        
        setContent {
            TalkingChildrenWearOSTheme {
                WearAppNavigation(
                    modifier = Modifier.fillMaxSize(),
                    hasHeartRatePermission = hasHeartRatePermission,
                    hasActivityPermission = hasActivityPermission,
                    onRequestPermissions = { requestPermissionsIfNeeded() }
                )
            }
        }
    }
    
    private fun checkPermissions() {
        hasHeartRatePermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED
        
        hasActivityPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
        
        hasBodySensorsPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestPermissionsIfNeeded() {
        val permissionsToRequest = mutableListOf<String>()
        
        if (!hasHeartRatePermission) {
            permissionsToRequest.add(Manifest.permission.BODY_SENSORS)
        }
        
        if (!hasActivityPermission) {
            permissionsToRequest.add(Manifest.permission.ACTIVITY_RECOGNITION)
        }
        
        if (!hasBodySensorsPermission) {
            permissionsToRequest.add(Manifest.permission.BODY_SENSORS)
        }
        
        if (permissionsToRequest.isNotEmpty()) {
            permissionLauncher.launch(permissionsToRequest.toTypedArray())
        }
    }
    
    companion object {
        private const val TAG = "MainActivity"
    }
}