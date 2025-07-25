package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.blxckbxll24.talkingchildrenwearos.R

@Composable
fun WearAppNavigation(
    modifier: Modifier = Modifier,
    hasHeartRatePermission: Boolean,
    hasActivityPermission: Boolean,
    onRequestPermissions: () -> Unit,
    navController: NavHostController = rememberSwipeDismissableNavController()
) {
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                navController = navController,
                hasHeartRatePermission = hasHeartRatePermission,
                hasActivityPermission = hasActivityPermission,
                onRequestPermissions = onRequestPermissions
            )
        }
        
        composable("heart_rate") {
            HeartRateScreen(
                navController = navController,
                hasPermission = hasHeartRatePermission,
                onRequestPermission = onRequestPermissions
            )
        }
        
        composable("activity") {
            ActivityScreen(
                navController = navController,
                hasPermission = hasActivityPermission,
                onRequestPermission = onRequestPermissions
            )
        }
        
        composable("settings") {
            SettingsScreen(
                navController = navController
            )
        }
    }
}