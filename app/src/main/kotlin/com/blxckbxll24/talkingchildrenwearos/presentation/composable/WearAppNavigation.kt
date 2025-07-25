package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Text
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import com.blxckbxll24.talkingchildrenwearos.R
import com.blxckbxll24.talkingchildrenwearos.presentation.theme.TalkingChildrenWearOSTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.blxckbxll24.talkingchildrenwearos.presentation.viewmodel.MessagesViewModel

@Composable
fun WearAppNavigation(
    modifier: Modifier = Modifier,
    hasHeartRatePermission: Boolean,
    hasActivityPermission: Boolean,
    onRequestPermissions: () -> Unit
) {
    val messagesViewModel: MessagesViewModel = viewModel()
    val navController = rememberSwipeDismissableNavController()
    
    SwipeDismissableNavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                viewModel = messagesViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        
        composable("home") {
            HomeScreen(
                onNavigateToHeartRate = { navController.navigate("heart_rate") },
                onNavigateToActivity = { navController.navigate("activity") },
                onNavigateToMessages = { navController.navigate("messages") },
                onNavigateToSettings = { navController.navigate("settings") },
                hasHeartRatePermission = hasHeartRatePermission,
                hasActivityPermission = hasActivityPermission,
                onRequestPermissions = onRequestPermissions
            )
        }
        
        composable("messages") {
            MessagesScreen(
                viewModel = messagesViewModel,
                onNavigateToLogin = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
        
        composable("heart_rate") {
            // Placeholder simple
            com.blxckbxll24.talkingchildrenwearos.presentation.screen.HeartRateScreen()
        }
        
        composable("activity") {
            // Placeholder simple
            com.blxckbxll24.talkingchildrenwearos.presentation.screen.ActivityScreen()
        }
        
        composable("settings") {
            com.blxckbxll24.talkingchildrenwearos.presentation.screen.SettingsScreen()
        }
    }
}

@Composable
fun MainScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("TalkingChildren WearOS")
        
        Button(
            onClick = { navController.navigate("heart_rate") }
        ) {
            Text("Heart Rate")
        }
        
        Button(
            onClick = { navController.navigate("activity") }
        ) {
            Text("Activity")
        }
        
        Button(
            onClick = { navController.navigate("settings") }
        ) {
            Text("Settings")
        }
    }
}