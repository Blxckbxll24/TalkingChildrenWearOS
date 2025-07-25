package com.blxckbxll24.talkingchildrenwearos.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.TimeText
import com.blxckbxll24.talkingchildrenwearos.presentation.composable.WearAppNavigation
import com.blxckbxll24.talkingchildrenwearos.presentation.theme.TalkingChildrenWearOSTheme

class MainActivity : ComponentActivity() {
    
    private var hasHeartRatePermission by mutableStateOf(false)
    private var hasActivityPermission by mutableStateOf(false)
    
    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasHeartRatePermission = permissions[Manifest.permission.BODY_SENSORS] == true
        hasActivityPermission = permissions[Manifest.permission.ACTIVITY_RECOGNITION] == true
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Check initial permissions
        checkPermissions()
        
        setTheme(android.R.style.Theme_DeviceDefault)
        
        setContent {
            WearApp(
                hasHeartRatePermission = hasHeartRatePermission,
                hasActivityPermission = hasActivityPermission,
                onRequestPermissions = ::requestPermissions
            )
        }
    }
    
    private fun checkPermissions() {
        hasHeartRatePermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.BODY_SENSORS
        ) == PackageManager.PERMISSION_GRANTED
        
        hasActivityPermission = ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
    }
    
    private fun requestPermissions() {
        val permissions = arrayOf(
            Manifest.permission.BODY_SENSORS,
            Manifest.permission.ACTIVITY_RECOGNITION
        )
        permissionLauncher.launch(permissions)
    }
}

@Composable
fun WearApp(
    hasHeartRatePermission: Boolean,
    hasActivityPermission: Boolean,
    onRequestPermissions: () -> Unit
) {
    TalkingChildrenWearOSTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            TimeText()
            WearAppNavigation(
                hasHeartRatePermission = hasHeartRatePermission,
                hasActivityPermission = hasActivityPermission,
                onRequestPermissions = onRequestPermissions
            )
        }
    }
}