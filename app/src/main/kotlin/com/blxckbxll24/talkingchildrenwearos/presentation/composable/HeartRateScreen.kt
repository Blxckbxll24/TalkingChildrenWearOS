package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.wear.compose.material.*
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.CircularProgressIndicator
import androidx.wear.compose.material.Text
import com.blxckbxll24.talkingchildrenwearos.presentation.viewmodel.HeartRateViewModel
import java.time.format.DateTimeFormatter

@Composable
fun HeartRateScreen(
    navController: NavController,
    viewModel: HeartRateViewModel
) {
    val heartRateState by viewModel.heartRateState.collectAsState()
    val isMonitoring by viewModel.isMonitoring.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        
        Text(
            text = "Heart Rate",
            style = MaterialTheme.typography.title3,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (isMonitoring) {
            CircularProgressIndicator(
                modifier = Modifier.size(60.dp),
                strokeWidth = 4.dp
            )
        } else {
            Text(
                text = "${heartRateState.currentHeartRate}",
                style = MaterialTheme.typography.display1,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "BPM",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        heartRateState.lastMeasurement?.let { lastTime ->
            Text(
                text = "Last: ${lastTime.format(DateTimeFormatter.ofPattern("HH:mm"))}",
                style = MaterialTheme.typography.caption1,
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = { // Agregar onClick faltante
                if (isMonitoring) {
                    viewModel.stopMonitoring()
                } else {
                    viewModel.startMonitoring()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (isMonitoring) "Stop" else "Start",
                textAlign = TextAlign.Center
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = if (heartRateState.isConnected) "Connected" else "Disconnected",
            style = MaterialTheme.typography.caption1,
            color = if (heartRateState.isConnected) Color.Green else Color.Red, // Cambiar 'color' por parámetro válido
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { // Agregar onClick faltante
                viewModel.simulateHeartRate()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Simulate",
                textAlign = TextAlign.Center
            )
        }
        
        heartRateState.error?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                style = MaterialTheme.typography.caption1,
                color = Color.Red,
                textAlign = TextAlign.Center
            )
        }
    }
}