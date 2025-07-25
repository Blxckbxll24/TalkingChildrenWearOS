package com.talkingchildren.wearos

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.speech.tts.TextToSpeech
import android.util.Log
import com.talkingchildren.wearos.models.Message
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Manages audio recording, playback, and text-to-speech functionality
 */
class AudioManager(private val context: Context) {
    
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var textToSpeech: TextToSpeech? = null
    private var isRecording = false
    private var isPlaying = false
    private var currentRecordingFile: File? = null
    
    companion object {
        private const val TAG = "AudioManager"
        private const val MAX_RECORDING_DURATION = 10000 // 10 seconds
    }
    
    interface AudioCallback {
        fun onRecordingStarted()
        fun onRecordingStopped()
        fun onPlaybackStarted()
        fun onPlaybackStopped()
        fun onError(message: String)
        fun onTTSReady()
    }
    
    private var callback: AudioCallback? = null
    
    fun setCallback(callback: AudioCallback) {
        this.callback = callback
    }
    
    /**
     * Initialize TTS engine
     */
    fun initializeTTS() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech?.setLanguage(Locale("es", "ES"))
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.w(TAG, "Spanish language not supported, using default")
                }
                callback?.onTTSReady()
            } else {
                callback?.onError("Failed to initialize Text-to-Speech")
            }
        }
    }
    
    /**
     * Start recording audio
     */
    fun startRecording(categoryId: String): File? {
        if (isRecording) {
            Log.w(TAG, "Already recording")
            return null
        }
        
        try {
            val audioDir = getAudioDirectory(categoryId)
            if (!audioDir.exists()) {
                audioDir.mkdirs()
            }
            
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            val fileName = "recording_${timestamp}.3gp"
            currentRecordingFile = File(audioDir, fileName)
            
            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(currentRecordingFile!!.absolutePath)
                setMaxDuration(MAX_RECORDING_DURATION)
                
                setOnInfoListener { _, what, _ ->
                    if (what == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                        stopRecording()
                    }
                }
                
                prepare()
                start()
            }
            
            isRecording = true
            callback?.onRecordingStarted()
            Log.d(TAG, "Recording started: ${currentRecordingFile!!.absolutePath}")
            
            return currentRecordingFile
            
        } catch (e: IOException) {
            Log.e(TAG, "Failed to start recording", e)
            callback?.onError("Failed to start recording: ${e.message}")
            return null
        }
    }
    
    /**
     * Stop recording audio
     */
    fun stopRecording(): File? {
        if (!isRecording) {
            Log.w(TAG, "Not currently recording")
            return null
        }
        
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null
            isRecording = false
            
            callback?.onRecordingStopped()
            Log.d(TAG, "Recording stopped: ${currentRecordingFile?.absolutePath}")
            
            return currentRecordingFile
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to stop recording", e)
            callback?.onError("Failed to stop recording: ${e.message}")
            return null
        }
    }
    
    /**
     * Play a message (either TTS for predefined or audio file for recorded)
     */
    fun playMessage(message: Message) {
        when (message.type) {
            Message.MessageType.PREDEFINED -> playTTS(message.text)
            Message.MessageType.RECORDED -> playAudioFile(message.audioFilePath)
        }
    }
    
    /**
     * Play text using TTS
     */
    private fun playTTS(text: String) {
        if (textToSpeech == null) {
            callback?.onError("Text-to-Speech not initialized")
            return
        }
        
        callback?.onPlaybackStarted()
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "TTS_$text")
    }
    
    /**
     * Play audio file
     */
    private fun playAudioFile(filePath: String?) {
        if (filePath == null) {
            callback?.onError("No audio file path provided")
            return
        }
        
        val file = File(filePath)
        if (!file.exists()) {
            callback?.onError("Audio file not found")
            return
        }
        
        stopPlayback() // Stop any current playback
        
        try {
            mediaPlayer = MediaPlayer().apply {
                setDataSource(filePath)
                setOnCompletionListener {
                    isPlaying = false
                    callback?.onPlaybackStopped()
                }
                setOnErrorListener { _, what, extra ->
                    Log.e(TAG, "MediaPlayer error: what=$what, extra=$extra")
                    callback?.onError("Playback error")
                    true
                }
                prepare()
                start()
            }
            
            isPlaying = true
            callback?.onPlaybackStarted()
            Log.d(TAG, "Playing audio file: $filePath")
            
        } catch (e: IOException) {
            Log.e(TAG, "Failed to play audio file", e)
            callback?.onError("Failed to play audio: ${e.message}")
        }
    }
    
    /**
     * Stop current playback
     */
    fun stopPlayback() {
        mediaPlayer?.apply {
            if (isPlaying()) {
                stop()
            }
            release()
        }
        mediaPlayer = null
        isPlaying = false
        callback?.onPlaybackStopped()
    }
    
    /**
     * Get the audio directory for a specific category
     */
    private fun getAudioDirectory(categoryId: String): File {
        return File(context.getExternalFilesDir(null), "audio/$categoryId")
    }
    
    /**
     * Get all recorded messages for a category
     */
    fun getRecordedMessages(categoryId: String): List<Message> {
        val audioDir = getAudioDirectory(categoryId)
        if (!audioDir.exists()) {
            return emptyList()
        }
        
        return audioDir.listFiles { file -> file.extension == "3gp" }
            ?.map { file ->
                Message.createRecorded(
                    id = file.nameWithoutExtension,
                    text = "Mensaje grabado",
                    categoryId = categoryId,
                    audioFilePath = file.absolutePath
                )
            } ?: emptyList()
    }
    
    /**
     * Delete a recorded message file
     */
    fun deleteRecordedMessage(message: Message): Boolean {
        if (message.type != Message.MessageType.RECORDED || message.audioFilePath == null) {
            return false
        }
        
        val file = File(message.audioFilePath)
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }
    
    /**
     * Release all resources
     */
    fun release() {
        stopRecording()
        stopPlayback()
        textToSpeech?.shutdown()
        textToSpeech = null
    }
    
    // Getters for state
    fun isRecording() = isRecording
    fun isPlaying() = isPlaying
}