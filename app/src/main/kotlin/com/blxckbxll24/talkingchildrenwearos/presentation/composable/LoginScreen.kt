package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.*
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import com.blxckbxll24.talkingchildrenwearos.presentation.viewmodel.MessagesViewModel

@Composable
fun LoginScreen(
    viewModel: MessagesViewModel,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(false)
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onLoginSuccess()
        }
    }
    
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
            // Email input usando Card simple para Wear OS
            Card(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Email:",
                        style = MaterialTheme.typography.caption1
                    )
                    Text(
                        text = if (email.isEmpty()) "ana.nina@wearext.com" else email,
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
        
        item {
            // Password input
            Card(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Password:",
                        style = MaterialTheme.typography.caption1
                    )
                    Text(
                        text = if (password.isEmpty()) "nina123" else "••••••",
                        style = MaterialTheme.typography.body2
                    )
                }
            }
        }
        
        item {
            Button(
                onClick = {
                    val finalEmail = if (email.isEmpty()) "ana.nina@wearext.com" else email
                    val finalPassword = if (password.isEmpty()) "nina123" else password
                    viewModel.login(finalEmail, finalPassword)
                },
                enabled = !isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Login")
                }
            }
        }
        
        error?.let { errorMessage ->
            item {
                Card(
                    onClick = { viewModel.clearError() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.caption1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}