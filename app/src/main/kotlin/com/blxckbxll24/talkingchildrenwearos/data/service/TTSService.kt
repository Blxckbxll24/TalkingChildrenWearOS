package com.blxckbxll24.talkingchildrenwearos.data.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*

class TTSService(private val context: Context) {
    
    private var tts: TextToSpeech? = null
    private val _isReady = MutableStateFlow(false)
    val isReady: StateFlow<Boolean> = _isReady
    
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking
    
    init {
        initializeTTS()
    }
    
    private fun initializeTTS() {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.getDefault()
                tts?.setSpeechRate(0.9f) // Velocidad ligeramente más lenta para niños
                
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        _isSpeaking.value = true
                    }
                    
                    override fun onDone(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                    
                    override fun onError(utteranceId: String?) {
                        _isSpeaking.value = false
                    }
                })
                
                _isReady.value = true
            }
        }
    }
    
    fun speak(text: String, messageId: String = "") {
        if (_isReady.value) {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, messageId)
        }
    }
    
    fun stop() {
        tts?.stop()
        _isSpeaking.value = false
    }
    
    fun shutdown() {
        tts?.shutdown()
        tts = null
    }
}