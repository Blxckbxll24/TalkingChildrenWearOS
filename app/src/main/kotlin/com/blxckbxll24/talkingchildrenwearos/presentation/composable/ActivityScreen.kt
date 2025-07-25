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
import com.blxckbxll24.talkingchildrenwearos.presentation.theme.WearColors

@Composable
fun ActivityScreen(
    navController: NavHostController,
    hasPermission: Boolean,
    onRequestPermission: () -> Unit
) {
    var steps by remember { mutableIntStateOf(0) }
    var distance by remember { mutableFloatStateOf(0f) }
    var calories by remember { mutableIntStateOf(0) }
    
    // Simulate some activity data if permission is granted
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            steps = (5000..15000).random()
            distance = steps * 0.0008f
            calories = (steps * 0.04f).toInt()
        }
    }
    
    ScalingLazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = stringResource(R.string.activity_title),
                style = MaterialTheme.typography.title2,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
        
        if (!hasPermission) {
            item {
                PermissionRequiredCard(
                    title = stringResource(R.string.permission_activity),
                    onRequestPermission = onRequestPermission
                )
            }
        } else {
            item {
                ActivityStatsGrid(
                    steps = steps,
                    distance = distance,
                    calories = calories
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                DailyGoalCard(
                    currentSteps = steps,
                    goalSteps = 10000
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            item {
                ActivityHistoryCard()
            }
        }
    }
}

@Composable
fun ActivityStatsGrid(
    steps: Int,
    distance: Float,
    calories: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Steps Card
        ActivityStatCard(
            title = "Steps",
            value = steps.toString(),
            unit = "",
            color = WearColors.StepsColor,
            icon = "👟"
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Distance and Calories Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActivityStatCard(
                title = "Distance",
                value = String.format("%.2f", distance),
                unit = "km",
                color = WearColors.DistanceColor,
                icon = "📍",
                modifier = Modifier.weight(1f).padding(end = 4.dp)
            )
            
            ActivityStatCard(
                title = "Calories",
                value = calories.toString(),
                unit = "kcal",
                color = WearColors.CaloriesColor,
                icon = "🔥",
                modifier = Modifier.weight(1f).padding(start = 4.dp)
            )
        }
    }
}

@Composable
fun ActivityStatCard(
    title: String,
    value: String,
    unit: String,
    color: androidx.compose.ui.graphics.Color,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text(
                text = value,
                style = MaterialTheme.typography.title3,
                color = color
            )
            
            if (unit.isNotEmpty()) {
                Text(
                    text = unit,
                    style = MaterialTheme.typography.caption1,
                    color = color
                )
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.caption1,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}

@Composable
fun DailyGoalCard(
    currentSteps: Int,
    goalSteps: Int
) {
    val progress = (currentSteps.toFloat() / goalSteps).coerceAtMost(1f)
    val progressPercentage = (progress * 100).toInt()
    
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Daily Goal",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            CircularProgressIndicator(
                progress = progress,
                modifier = Modifier.size(60.dp),
                color = if (progress >= 1f) WearColors.HeartRateNormal else MaterialTheme.colors.primary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "$progressPercentage%",
                style = MaterialTheme.typography.title3,
                color = if (progress >= 1f) WearColors.HeartRateNormal else MaterialTheme.colors.primary
            )
            
            Text(
                text = "$currentSteps / $goalSteps steps",
                style = MaterialTheme.typography.caption1,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ActivityHistoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Weekly Summary",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "🏃‍♂️ Avg: 8,500 steps/day",
                style = MaterialTheme.typography.caption1,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            
            Text(
                text = "🎯 Goals met: 5/7 days",
                style = MaterialTheme.typography.caption1,
                modifier = Modifier.padding(vertical = 2.dp)
            )
            
            Text(
                text = "📈 Best day: 12,450 steps",
                style = MaterialTheme.typography.caption1,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}