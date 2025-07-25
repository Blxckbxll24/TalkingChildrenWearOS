package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.*
import com.blxckbxll24.talkingchildrenwearos.R

@Composable
fun SettingsScreen(
    navController: NavHostController
) {
    var enableNotifications by remember { mutableStateOf(true) }
    var autoHeartRate by remember { mutableStateOf(false) }
    var syncCompanion by remember { mutableStateOf(true) }
    
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.settings_title),
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        item {
            SettingsToggleCard(
                title = stringResource(R.string.enable_notifications),
                description = "Get alerts for health data",
                checked = enableNotifications,
                onCheckedChange = { enableNotifications = it }
            )
        }
        
        item {
            SettingsToggleCard(
                title = stringResource(R.string.auto_heart_rate),
                description = "Automatic heart rate monitoring",
                checked = autoHeartRate,
                onCheckedChange = { autoHeartRate = it }
            )
        }
        
        item {
            SettingsToggleCard(
                title = stringResource(R.string.sync_companion),
                description = "Sync data with phone app",
                checked = syncCompanion,
                onCheckedChange = { syncCompanion = it }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            SettingsActionCard(
                title = "Heart Rate Thresholds",
                description = "Set high/low HR alerts",
                onClick = { /* Navigate to threshold settings */ }
            )
        }
        
        item {
            SettingsActionCard(
                title = "Daily Step Goal",
                description = "Current: 10,000 steps",
                onClick = { /* Navigate to goal settings */ }
            )
        }
        
        item {
            SettingsActionCard(
                title = "Data Management",
                description = "Export or clear data",
                onClick = { /* Navigate to data management */ }
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        item {
            AboutCard()
        }
    }
}

@Composable
fun SettingsToggleCard(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.caption1,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
            
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
    }
}

@Composable
fun SettingsActionCard(
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1
            )
            Text(
                text = description,
                style = MaterialTheme.typography.caption1,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun AboutCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.about),
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Talking Children Wear OS",
                style = MaterialTheme.typography.caption1,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Version 1.0",
                style = MaterialTheme.typography.caption1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "A comprehensive health and activity tracker for Wear OS",
                style = MaterialTheme.typography.caption1,
                textAlign = TextAlign.Center
            )
        }
    }
}