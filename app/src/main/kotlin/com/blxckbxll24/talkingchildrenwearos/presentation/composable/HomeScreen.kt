package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.material.*
import com.blxckbxll24.talkingchildrenwearos.R
import com.blxckbxll24.talkingchildrenwearos.domain.model.NavigationItem
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HomeScreen(
    navController: NavHostController,
    hasHeartRatePermission: Boolean,
    hasActivityPermission: Boolean,
    onRequestPermissions: () -> Unit
) {
    val navigationItems = remember {
        listOf(
            NavigationItem("home", "Home", R.drawable.ic_home, "home"),
            NavigationItem("heart_rate", "Heart Rate", R.drawable.ic_heart_rate, "heart_rate"),
            NavigationItem("activity", "Activity", R.drawable.ic_activity, "activity"),
            NavigationItem("settings", "Settings", R.drawable.ic_settings, "settings")
        )
    }
    
    val currentTime = remember {
        SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
    }
    
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.welcome_message),
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        item {
            Text(
                text = currentTime,
                style = MaterialTheme.typography.title3,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        if (!hasHeartRatePermission || !hasActivityPermission) {
            item {
                Card(
                    onClick = onRequestPermissions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Permissions Required",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Tap to grant permissions",
                            style = MaterialTheme.typography.caption1,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        itemsIndexed(navigationItems) { index, item ->
            NavigationCard(
                title = item.title,
                onClick = { 
                    if (item.route != "home") {
                        navController.navigate(item.route)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            )
        }
        
        item {
            QuickStatsCard(
                hasHeartRatePermission = hasHeartRatePermission,
                hasActivityPermission = hasActivityPermission
            )
        }
    }
}

@Composable
fun NavigationCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun QuickStatsCard(
    hasHeartRatePermission: Boolean,
    hasActivityPermission: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Quick Stats",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            if (hasHeartRatePermission) {
                Text(
                    text = "❤️ -- BPM",
                    style = MaterialTheme.typography.caption1
                )
            }
            
            if (hasActivityPermission) {
                Text(
                    text = "👟 -- Steps",
                    style = MaterialTheme.typography.caption1
                )
            }
            
            if (!hasHeartRatePermission && !hasActivityPermission) {
                Text(
                    text = "Enable permissions to see stats",
                    style = MaterialTheme.typography.caption1,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}