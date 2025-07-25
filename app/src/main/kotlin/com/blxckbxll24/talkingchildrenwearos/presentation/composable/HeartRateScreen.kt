package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.*
import com.blxckbxll24.talkingchildrenwearos.R
import com.blxckbxll24.talkingchildrenwearos.presentation.theme.WearColors

@Composable
fun HeartRateScreen(
    navController: NavHostController,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    var heartRate by remember { mutableIntStateOf(0) }
    var isMeasuring by remember { mutableStateOf(false) }
    var isAvailable by remember { mutableStateOf(hasPermission) }
    
    LaunchedEffect(hasPermission) {
        isAvailable = hasPermission
    }
    
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.heart_rate_title),
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        if (!isAvailable) {
            item {
                PermissionRequiredCard(
                    title = stringResource(R.string.permission_heart_rate),
                    onRequestPermission = onRequestPermission
                )
            }
        } else {
            item {
                HeartRateDisplay(
                    heartRate = heartRate,
                    isMeasuring = isMeasuring
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                Button(
                    onClick = {
                        isMeasuring = !isMeasuring
                        if (isMeasuring) {
                            // Simulate heart rate measurement
                            heartRate = (60..100).random()
                        }
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                ) {
                    Text(
                        text = if (isMeasuring) "Stop" else "Start",
                        style = MaterialTheme.typography.button
                    )
                }
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                HeartRateHistory()
            }
        }
    }
}

@Composable
fun HeartRateDisplay(
    heartRate: Int,
    isMeasuring: Boolean
) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isMeasuring && heartRate == 0) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = WearColors.HeartRateNormal
                    )
                    Text(
                        text = stringResource(R.string.heart_rate_measuring),
                        style = MaterialTheme.typography.caption1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                } else if (heartRate > 0) {
                    Text(
                        text = "$heartRate",
                        style = MaterialTheme.typography.display1,
                        color = getHeartRateColor(heartRate)
                    )
                    Text(
                        text = "BPM",
                        style = MaterialTheme.typography.caption1,
                        color = getHeartRateColor(heartRate)
                    )
                } else {
                    Text(
                        text = "❤️",
                        style = MaterialTheme.typography.display1
                    )
                    Text(
                        text = "Tap Start",
                        style = MaterialTheme.typography.caption1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun HeartRateHistory() {
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
                text = "Recent Readings",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Placeholder for recent readings
            Text(
                text = "No recent readings",
                style = MaterialTheme.typography.caption1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun PermissionRequiredCard(
    title: String,
    onRequestPermission: () -> Unit
) {
    Card(
        onClick = onRequestPermission,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = stringResource(R.string.grant_permission),
                style = MaterialTheme.typography.caption1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun getHeartRateColor(heartRate: Int) = when {
    heartRate < 60 || heartRate > 140 -> WearColors.HeartRateHigh
    heartRate > 100 -> WearColors.HeartRateElevated
    else -> WearColors.HeartRateNormal
}