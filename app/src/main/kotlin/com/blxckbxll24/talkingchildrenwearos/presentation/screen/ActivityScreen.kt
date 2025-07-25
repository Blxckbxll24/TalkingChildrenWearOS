package com.blxckbxll24.talkingchildrenwearos.presentation.screen

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.material.*

@Composable
fun ActivityScreen() {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Activity",
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center
            )
        }
        item {
            Text(
                text = "Coming soon...",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }
}