package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*

@Composable
fun ActivityScreen() {
    var steps by remember { mutableStateOf(0) }
    var distance by remember { mutableStateOf(0f) }
    var calories by remember { mutableStateOf(0) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Actividad",
            style = MaterialTheme.typography.title1
        )
        
        // Steps Card
        Card(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsWalk,
                    contentDescription = "Pasos"
                )
                Text(
                    text = "$steps",
                    style = MaterialTheme.typography.title2
                )
                Text(
                    text = "Pasos",
                    style = MaterialTheme.typography.body2
                )
            }
        }
        
        // Distance Card
        Card(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Timeline,
                    contentDescription = "Distancia"
                )
                Text(
                    text = "${distance}km",
                    style = MaterialTheme.typography.title2
                )
                Text(
                    text = "Distancia",
                    style = MaterialTheme.typography.body2
                )
            }
        }
        
        // Progress indicator
        CircularProgressIndicator(
            progress = (steps / 10000f).coerceAtMost(1f),
            modifier = Modifier.size(60.dp),
            strokeWidth = 4.dp
        )
        
        // Calories Card
        Card(
            onClick = { },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = "Calorías"
                )
                Text(
                    text = "$calories",
                    style = MaterialTheme.typography.title2
                )
                Text(
                    text = "Calorías",
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
}