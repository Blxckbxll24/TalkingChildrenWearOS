package com.blxckbxll24.talkingchildrenwearos.presentation.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

internal val wearColorPalette: Colors = Colors(
    primary = Color(0xFF2196F3),
    primaryVariant = Color(0xFF1976D2),
    secondary = Color(0xFFFF5722),
    secondaryVariant = Color(0xFFE64A19),
    background = Color(0xFF000000),
    surface = Color(0xFF121212),
    error = Color(0xFFF44336),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.White
)

@Composable
fun TalkingChildrenWearOSTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = wearColorPalette,
        content = content
    )
}

// Additional color definitions for specific use cases
object WearColors {
    val HeartRateNormal = Color(0xFF4CAF50)
    val HeartRateElevated = Color(0xFFFF9800)
    val HeartRateHigh = Color(0xFFF44336)
    
    val StepsColor = Color(0xFF9C27B0)
    val DistanceColor = Color(0xFF00BCD4)
    val CaloriesColor = Color(0xFFFF5722)
    
    val SensorActive = Color(0xFF4CAF50)
    val SensorInactive = Color(0xFF757575)
    
    val NavSelected = Color(0xFF2196F3)
    val NavUnselected = Color(0xFF666666)
}