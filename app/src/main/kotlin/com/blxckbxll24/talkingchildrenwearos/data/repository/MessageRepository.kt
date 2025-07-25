package com.blxckbxll24.talkingchildrenwearos.data.repository

import android.content.Context
import android.util.Log
import com.blxckbxll24.talkingchildrenwearos.data.model.ChildMessage
import com.blxckbxll24.talkingchildrenwearos.data.model.MessageDetail
import com.blxckbxll24.talkingchildrenwearos.data.model.Category
import com.blxckbxll24.talkingchildrenwearos.data.model.AssignedBy
import com.blxckbxll24.talkingchildrenwearos.data.model.AuthResponse
import com.blxckbxll24.talkingchildrenwearos.data.model.AuthRequest
import com.blxckbxll24.talkingchildrenwearos.data.model.User
import com.blxckbxll24.talkingchildrenwearos.data.model.ResponseData
import com.blxckbxll24.talkingchildrenwearos.data.model.ApiMessage
import com.blxckbxll24.talkingchildrenwearos.data.model.MessagesApiResponse
import com.blxckbxll24.talkingchildrenwearos.data.model.ChildMessageApiResponse
import com.blxckbxll24.talkingchildrenwearos.data.network.RetrofitClient
import kotlinx.coroutines.delay
import java.io.File

class MessageRepository(private val context: Context) {
    private val apiService = RetrofitClient.apiService
    private val tag = "MessageRepository"
    
    // ACTIVAR conexión real
    private val testMode = false  // VOLVER A false para modo real
    
    init {
        Log.d(tag, "🔧 MessageRepository inicializado - testMode: $testMode")
        Log.d(tag, "🔧 ApiService: $apiService")
    }
    
    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            if (testMode) {
                delay(1000)
                val authResponse = AuthResponse(
                    success = true,
                    message = "Login successful (test)",
                    data = ResponseData(
                        token = "test_token_12345",
                        user = User(
                            id = 1,
                            name = "Ana Nina",
                            email = email,
                            roleId = 1,
                            roleName = "niño"
                        )
                    )
                )
                Result.success(authResponse)
            } else {
                val request = AuthRequest(email, password)
                val response = apiService.login(request)
                
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    if (authResponse.success && !authResponse.actualToken.isNullOrEmpty()) {
                        Log.d(tag, "✅ Login exitoso! Token: ${authResponse.actualToken?.take(10)}...")
                        Result.success(authResponse)
                    } else {
                        Result.failure(Exception(authResponse.message))
                    }
                } else {
                    Result.failure(Exception("HTTP ${response.code()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "🚫 Error login: ${e.message}")
            Result.failure(e)
        }
    }
    
    suspend fun getMyMessages(token: String): Result<List<ChildMessage>> {
        return try {
            Log.d(tag, "============================================")
            Log.d(tag, "📱 OBTENIENDO MENSAJES")
            Log.d(tag, "🔑 Token: ${token.take(20)}...")
            Log.d(tag, "⚠️ testMode: $testMode")
            Log.d(tag, "🔍 ¿Es token offline?: ${token.startsWith("offline_")}")
            Log.d(tag, "🔍 ¿Es token test?: ${token.startsWith("test_")}")
            Log.d(tag, "============================================")
            
            if (testMode || token.startsWith("offline_") || token.startsWith("test_")) {
                Log.d(tag, "📱 USANDO MENSAJES DE PRUEBA")
                delay(800)
                val testMessages = createTestMessages()
                Log.d(tag, "📝 Creados ${testMessages.size} mensajes de prueba")
                Result.success(testMessages)
            } else {
                try {
                    Log.d(tag, "🔐 Obteniendo mensajes con token: ${token.take(10)}...")
                    val response = apiService.getMyMessages("Bearer $token")
                    
                    if (response.isSuccessful) {
                        val apiResponse = response.body()
                        Log.d(tag, "📡 Response completo: $apiResponse")
                        
                        // NUEVO: Usar la nueva estructura de API
                        val messagesArray = if (apiResponse?.success == true) {
                            apiResponse.data ?: emptyList()
                        } else {
                            emptyList()
                        }
                        
                        Log.d(tag, "📨 Recibidos ${messagesArray.size} mensajes desde API")
                        
                        val convertedMessages = convertApiMessages(messagesArray)
                        
                        Log.d(tag, "✅ ${convertedMessages.size} mensajes convertidos y listos")
                        Result.success(convertedMessages)
                    } else {
                        Log.e(tag, "❌ Error HTTP: ${response.code()} - ${response.message()}")
                        Result.failure(Exception("Error HTTP: ${response.code()}"))
                    }
                } catch (e: Exception) {
                    Log.e(tag, "❌ Error al obtener mensajes: ${e.message}")
                    Result.failure(e)
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "🚫 ERROR obteniendo mensajes: ${e.message}")
            
            // Fallback solo para errores de red
            if (e.message?.contains("ConnectException") == true || 
                e.message?.contains("UnknownHostException") == true) {
                Log.d(tag, "🔄 Red no disponible, usando mensajes de prueba...")
                Result.success(createTestMessages())
            } else {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getFavoriteMessages(token: String): Result<List<ChildMessage>> {
        return try {
            if (testMode || token.startsWith("offline_") || token.startsWith("test_")) {
                delay(600)
                val favoriteMessages = createTestMessages().filter { it.isFavorite }
                Result.success(favoriteMessages)
            } else {
                Log.d(tag, "🔐 Obteniendo favoritos con token...")
                
                val response = apiService.getFavoriteMessages("Bearer $token")
                
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success) {
                        val apiMessages = apiResponse.data ?: emptyList()
                        // USAR la nueva función de conversión
                        val childMessages = convertApiMessages(apiMessages)
                        Log.d(tag, "✅ ${childMessages.size} favoritos obtenidos desde BD")
                        Result.success(childMessages)
                    } else {
                        Result.failure(Exception(apiResponse.message ?: "Failed to get favorites"))
                    }
                } else {
                    Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "🚫 Error obteniendo favoritos", e)
            Result.failure(e)
        }
    }
    
    suspend fun toggleFavorite(messageId: Int, token: String, isFavorite: Boolean): Result<ChildMessage> {
        return try {
            if (testMode || token.startsWith("offline_") || token.startsWith("test_")) {
                delay(300)
                // Simular toggle de favorito
                val mockMessage = createTestMessages().find { it.id == messageId }
                    ?: createTestMessages().first()
                Result.success(mockMessage.copy(isFavorite = isFavorite))
            } else {
                Log.d(tag, "🔐 Actualizando favorito $messageId con token...")
                
                val updateData = mapOf("isFavorite" to isFavorite)
                val response = apiService.updateMessage(messageId, "Bearer $token", updateData)
                
                if (response.isSuccessful && response.body() != null) {
                    val apiResponse = response.body()!!
                    if (apiResponse.success && apiResponse.data != null) {
                        // USAR la nueva estructura ChildMessageApiResponse
                        val apiMessages = apiResponse.data ?: emptyList()
                        val apiMessage = apiMessages.firstOrNull() ?: return Result.failure(Exception("No message returned"))
                        
                        // CONVERTIR usando la nueva función
                        val convertedMessages = convertApiMessages(listOf(apiMessage))
                        val childMessage = convertedMessages.firstOrNull() ?: return Result.failure(Exception("Failed to convert message"))
                        
                        Log.d(tag, "✅ Favorito actualizado")
                        Result.success(childMessage.copy(isFavorite = isFavorite))
                    } else {
                        Result.failure(Exception(apiResponse.message ?: "Failed to update favorite"))
                    }
                } else {
                    Result.failure(Exception("HTTP ${response.code()}: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "🚫 Error actualizando favorito", e)
            Result.failure(e)
        }
    }
    
    suspend fun downloadAndCacheAudio(messageId: Int, token: String): Result<File> {
        return try {
            if (testMode || token.startsWith("offline_") || token.startsWith("test_")) {
                delay(500)
                val cacheFile = File(context.cacheDir, "audio_$messageId.wav")
                if (!cacheFile.exists()) {
                    cacheFile.createNewFile()
                }
                Result.success(cacheFile)
            } else {
                Log.d(tag, "🔐 Descargando audio $messageId con token...")
                
                val response = apiService.getAudioFile(messageId, "Bearer $token")
                
                if (response.isSuccessful && response.body() != null) {
                    val audioBytes = response.body()!!.bytes()
                    val cacheFile = File(context.cacheDir, "audio_$messageId.wav")
                    
                    cacheFile.writeBytes(audioBytes)
                    Log.d(tag, "✅ Audio descargado: ${cacheFile.length()} bytes")
                    Result.success(cacheFile)
                } else {
                    Result.failure(Exception("Failed to download audio: ${response.message()}"))
                }
            }
        } catch (e: Exception) {
            Log.e(tag, "🚫 Error descargando audio", e)
            Result.failure(e)
        }
    }
    
    private fun createTestMessages(): List<ChildMessage> {
        return listOf(
            ChildMessage(
                id = 999,
                message = MessageDetail(
                    id = 999,
                    text = "¡Hola mamá! ¿Cómo estás? (MODO PRUEBA)",
                    audioUrl = "audio999.wav",
                    category = "Saludos", // STRING, no Category
                    categoryColor = "#4CAF50",
                    categoryIcon = "👋"
                ),
                isFavorite = true,
                assignedAt = "2024-01-15T10:30:00Z",
                assignedBy = AssignedBy(1, "Test Mode")
            )
        )
    }
    
    private fun convertApiMessages(apiMessages: List<ChildMessageApiResponse>): List<ChildMessage> {
        Log.d(tag, "🔄 Convirtiendo ${apiMessages.size} mensajes de API")
        
        return apiMessages.mapNotNull { childMessage ->
            try {
                val message = childMessage.message
                val childMsg = ChildMessage(
                    id = childMessage.id,
                    message = MessageDetail(
                        id = message.id,
                        text = message.text,
                        audioUrl = message.audio_url,
                        category = message.category.name,
                        categoryColor = message.category.color,
                        categoryIcon = message.category.icon,
                        isFavorite = childMessage.is_favorite == 1,
                        assignedAt = childMessage.assigned_at,
                        assignedBy = childMessage.assigned_by.name
                    ),
                    isFavorite = childMessage.is_favorite == 1,
                    assignedAt = childMessage.assigned_at,
                    assignedBy = AssignedBy(
                        id = childMessage.assigned_by.id,
                        name = childMessage.assigned_by.name
                    )
                )
                Log.d(tag, "✅ Mensaje convertido: ${childMsg.message.text}")
                childMsg
            } catch (e: Exception) {
                Log.e(tag, "❌ Error convirtiendo mensaje: ${e.message}")
                null
            }
        }
    }
}