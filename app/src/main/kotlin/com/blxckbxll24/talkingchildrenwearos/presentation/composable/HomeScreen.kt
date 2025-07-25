package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.*

@Composable
fun HomeScreen(
    onNavigateToHeartRate: () -> Unit,
    onNavigateToActivity: () -> Unit,
    onNavigateToMessages: () -> Unit,
    onNavigateToSettings: () -> Unit,
    hasHeartRatePermission: Boolean,
    hasActivityPermission: Boolean,
    onRequestPermissions: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Talking Children",
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        item {
            Card(
                onClick = onNavigateToMessages,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "💬",
                        style = MaterialTheme.typography.display1
                    )
                    Text(
                        text = "Messages",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        item {
            Card(
                onClick = {
                    if (hasHeartRatePermission) {
                        onNavigateToHeartRate()
                    } else {
                        onRequestPermissions()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "❤️",
                        style = MaterialTheme.typography.display1
                    )
                    Text(
                        text = "Heart Rate",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                    if (!hasHeartRatePermission) {
                        Text(
                            text = "Permission needed",
                            style = MaterialTheme.typography.caption2,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
        
        item {
            Card(
                onClick = {
                    if (hasActivityPermission) {
                        onNavigateToActivity()
                    } else {
                        onRequestPermissions()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🏃",
                        style = MaterialTheme.typography.display1
                    )
                    Text(
                        text = "Activity",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                    if (!hasActivityPermission) {
                        Text(
                            text = "Permission needed",
                            style = MaterialTheme.typography.caption2,
                            color = MaterialTheme.colors.error
                        )
                    }
                }
            }
        }
        
        item {
            Card(
                onClick = onNavigateToSettings,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "⚙️",
                        style = MaterialTheme.typography.display1
                    )
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.body1,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}