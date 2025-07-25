package com.blxckbxll24.talkingchildrenwearos.presentation.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material.*
import com.blxckbxll24.talkingchildrenwearos.data.model.ChildMessage
import com.blxckbxll24.talkingchildrenwearos.presentation.viewmodel.MessagesViewModel

@Composable
fun MessagesScreen(
    viewModel: MessagesViewModel,
    onNavigateToLogin: () -> Unit
) {
    val isLoggedIn by viewModel.isLoggedIn.observeAsState(false)
    val messages by viewModel.messages.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val error by viewModel.error.observeAsState()
    val isPlaying by viewModel.isPlaying.observeAsState(false)
    val currentMessage by viewModel.currentMessage.observeAsState("")
    
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            viewModel.loadMessages()
        }
    }
    
    if (!isLoggedIn) {
        onNavigateToLogin()
        return
    }
    
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header con indicador de reproducción
        if (isPlaying && currentMessage.isNotEmpty()) {
            Card(
                onClick = { /* No action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primary)
                        .padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🔊 REPRODUCIENDO",
                        style = MaterialTheme.typography.caption1,
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = currentMessage,
                        style = MaterialTheme.typography.caption2,
                        color = MaterialTheme.colors.onPrimary,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    // Controles de reproducción
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { viewModel.pauseAudio() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("⏸️", style = MaterialTheme.typography.caption2)
                        }
                        
                        Button(
                            onClick = { viewModel.stopAudio() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Text("⏹️", style = MaterialTheme.typography.caption2)
                        }
                    }
                }
            }
        }
        
        // Lista de mensajes
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                Text(
                    text = if (messages.isEmpty()) "Cargando mensajes..." else "Mis Mensajes",
                    style = MaterialTheme.typography.title3,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            
            if (isLoading) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            error?.let { errorMessage ->
                item {
                    Text(
                        text = "Error: $errorMessage",
                        style = MaterialTheme.typography.caption2,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            items(messages) { message ->
                MessageCard(
                    message = message,
                    isCurrentlyPlaying = isPlaying && currentMessage == message.message.text,
                    onPlayClick = { viewModel.playMessage(message) },
                    onFavoriteClick = { viewModel.toggleFavorite(message) }
                )
            }
        }
    }
}

@Composable
fun MessageCard(
    message: ChildMessage,
    isCurrentlyPlaying: Boolean,
    onPlayClick: () -> Unit,
    onFavoriteClick: () -> Unit
) {
    Card(
        onClick = onPlayClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .background(
                    if (isCurrentlyPlaying) 
                        MaterialTheme.colors.primary.copy(alpha = 0.3f) 
                    else 
                        MaterialTheme.colors.surface
                )
                .padding(12.dp)
        ) {
            // Categoría con ícono
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = message.message.categoryIcon, // Cambiar: message.categoryIcon -> message.message.categoryIcon
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = message.message.category, // Cambiar: message.category -> message.message.category
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            // Texto del mensaje
            Text(
                text = message.message.text,
                style = MaterialTheme.typography.body2,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Controles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Indicador de reproducción
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            if (isCurrentlyPlaying) 
                                MaterialTheme.colors.primary 
                            else 
                                MaterialTheme.colors.primaryVariant,
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isCurrentlyPlaying) "🔊" else "▶️",
                        style = MaterialTheme.typography.caption1,
                        color = Color.White
                    )
                }
                
                // Botón de favorito
                Button(
                    onClick = onFavoriteClick,
                    modifier = Modifier.size(36.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (message.isFavorite) 
                            Color.Red.copy(alpha = 0.7f) 
                        else 
                            MaterialTheme.colors.surface
                    )
                ) {
                    Text(
                        text = if (message.isFavorite) "❤️" else "🤍",
                        style = MaterialTheme.typography.caption1
                    )
                }
            }
        }
    }
}