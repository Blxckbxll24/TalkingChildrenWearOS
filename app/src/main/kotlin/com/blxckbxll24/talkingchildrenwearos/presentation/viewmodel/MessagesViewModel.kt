package com.blxckbxll24.talkingchildrenwearos.presentation.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blxckbxll24.talkingchildrenwearos.data.repository.MessageRepository
import com.blxckbxll24.talkingchildrenwearos.data.service.AudioPlayerService
import com.blxckbxll24.talkingchildrenwearos.data.model.ChildMessage
import kotlinx.coroutines.launch
import java.io.File

class MessagesViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = MessageRepository(application)
    private val audioPlayerService = AudioPlayerService(application)
    private val tag = "MessagesViewModel"
    
    private val _messages = MutableLiveData<List<ChildMessage>>()
    val messages: LiveData<List<ChildMessage>> get() = _messages
    
    private val _favoriteMessages = MutableLiveData<List<ChildMessage>>(emptyList())
    val favoriteMessages: LiveData<List<ChildMessage>> = _favoriteMessages
    
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn
    
    private var authToken: String? = null
    
    // Audio player state
    val isPlaying: LiveData<Boolean> = audioPlayerService.isPlaying
    val currentMessage: LiveData<String> = audioPlayerService.currentMessage
    
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.login(email, password)
                .onSuccess { authResponse: com.blxckbxll24.talkingchildrenwearos.data.model.AuthResponse ->
                    authToken = authResponse.actualToken  // ⬅️ CAMBIAR: .token → .actualToken
                    _isLoggedIn.value = true
                    
                    // Save token in SharedPreferences
                    saveAuthToken(authResponse.actualToken ?: "")  // ⬅️ CAMBIAR: .token → .actualToken
                    
                    // Load messages after successful login
                    loadMessages()
                    Log.d(tag, "Login successful")
                }
                .onFailure { exception: Throwable ->
                    _error.value = "Login failed: ${exception.message}"
                    _isLoggedIn.value = false
                    Log.e(tag, "Login failed", exception)
                }
            
            _isLoading.value = false
        }
    }
    
    fun loadMessages() {
        val token = authToken ?: getStoredAuthToken()
        if (token.isBlank()) {
            _error.value = "No authentication token available"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val result = repository.getMyMessages(token)
            result.onSuccess { messageList: List<ChildMessage> ->
                _messages.value = messageList
                Log.d(tag, "Loaded ${messageList.size} messages")
            }
            .onFailure { exception: Throwable ->
                _error.value = "Failed to load messages: ${exception.message}"
                Log.e(tag, "Failed to load messages", exception)
            }
            
            _isLoading.value = false
        }
    }
    
    fun loadFavoriteMessages() {
        val token = authToken ?: getStoredAuthToken()
        if (token.isBlank()) {
            _error.value = "No authentication token available"
            return
        }
        
        viewModelScope.launch {
            _isLoading.value = true
            
            repository.getFavoriteMessages(token)
                .onSuccess { favoriteList: List<ChildMessage> ->
                    _favoriteMessages.value = favoriteList
                    Log.d(tag, "Loaded ${favoriteList.size} favorite messages")
                }
                .onFailure { exception: Throwable ->
                    _error.value = "Failed to load favorites: ${exception.message}"
                    Log.e(tag, "Failed to load favorites", exception)
                }
            
            _isLoading.value = false
        }
    }
    
    fun playMessage(message: ChildMessage) {
        val token = authToken ?: getStoredAuthToken()
        if (token.isBlank()) {
            _error.value = "No authentication token available"
            return
        }
        
        viewModelScope.launch {
            _error.value = null
            
            // Check if audio file exists in cache
            val cachedFile = File(getApplication<Application>().cacheDir, "audio_${message.message.id}.wav")
            
            if (cachedFile.exists()) {
                // Play cached audio
                audioPlayerService.playAudio(cachedFile, message.message.text)
            } else {
                // Download and cache audio
                repository.downloadAndCacheAudio(message.message.id, token)
                    .onSuccess { audioFile: File ->
                        audioPlayerService.playAudio(audioFile, message.message.text)
                        Log.d(tag, "Downloaded and playing audio for message: ${message.message.text}")
                    }
                    .onFailure { exception: Throwable ->
                        _error.value = "Failed to play audio: ${exception.message}"
                        Log.e(tag, "Failed to download audio", exception)
                    }
            }
        }
    }
    
    fun stopAudio() {
        audioPlayerService.stopAudio()
    }
    
    fun pauseAudio() {
        audioPlayerService.pauseAudio()
    }
    
    fun resumeAudio() {
        audioPlayerService.resumeAudio()
    }
    
    fun toggleFavorite(message: ChildMessage) {
        val token = authToken ?: getStoredAuthToken()
        if (token.isBlank()) {
            _error.value = "No authentication token available"
            return
        }
        
        viewModelScope.launch {
            repository.toggleFavorite(message.id, token, !message.isFavorite)
                .onSuccess { updatedMessage: ChildMessage ->
                    // Update the message in the list
                    val updatedList = _messages.value?.map { msg ->
                        if (msg.id == updatedMessage.id) updatedMessage else msg
                    } ?: emptyList()
                    _messages.value = updatedList
                    
                    Log.d(tag, "Toggled favorite for message: ${message.message.text}")
                }
                .onFailure { exception: Throwable ->
                    _error.value = "Failed to update favorite: ${exception.message}"
                    Log.e(tag, "Failed to toggle favorite", exception)
                }
        }
    }
    
    fun clearError() {
        _error.value = null
    }
    
    private fun saveAuthToken(token: String) {
        val sharedPref = getApplication<Application>().getSharedPreferences(
            "talking_children_prefs", Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString("auth_token", token)
            apply()
        }
    }
    
    private fun getStoredAuthToken(): String {
        val sharedPref = getApplication<Application>().getSharedPreferences(
            "talking_children_prefs", Context.MODE_PRIVATE
        )
        return sharedPref.getString("auth_token", "") ?: ""
    }
    
    fun logout() {
        authToken = null
        _isLoggedIn.value = false
        _messages.value = emptyList()
        _favoriteMessages.value = emptyList()
        audioPlayerService.release()
        
        // Clear stored token
        saveAuthToken("")
    }
    
    override fun onCleared() {
        super.onCleared()
        audioPlayerService.release()
    }
    
    init {
        // Check if user is already logged in
        val storedToken = getStoredAuthToken()
        if (storedToken.isNotBlank()) {
            authToken = storedToken
            _isLoggedIn.value = true
            loadMessages()
        }
    }
}