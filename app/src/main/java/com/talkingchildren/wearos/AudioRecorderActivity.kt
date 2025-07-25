package com.talkingchildren.wearos

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.File

/**
 * Activity for recording audio messages
 */
class AudioRecorderActivity : ComponentActivity(), AudioManager.AudioCallback {

    companion object {
        const val EXTRA_CATEGORY_ID = "category_id"
        private const val PERMISSION_REQUEST_RECORD_AUDIO = 100
        private const val MAX_RECORDING_TIME = 10000L // 10 seconds
    }

    private lateinit var statusText: TextView
    private lateinit var timerText: TextView
    private lateinit var recordButton: ImageButton
    private lateinit var stopButton: ImageButton
    private lateinit var playButton: ImageButton
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button
    
    private lateinit var audioManager: AudioManager
    private lateinit var categoryId: String
    
    private var recordingFile: File? = null
    private var recordingStartTime = 0L
    private val timerHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = object : Runnable {
        override fun run() {
            updateTimer()
            timerHandler.postDelayed(this, 100)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recorder)

        categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID) ?: "basic"

        setupViews()
        setupAudioManager()
        checkAudioPermission()
    }

    private fun setupViews() {
        statusText = findViewById(R.id.statusText)
        timerText = findViewById(R.id.timerText)
        recordButton = findViewById(R.id.recordButton)
        stopButton = findViewById(R.id.stopButton)
        playButton = findViewById(R.id.playButton)
        saveButton = findViewById(R.id.saveButton)
        cancelButton = findViewById(R.id.cancelButton)

        recordButton.setOnClickListener {
            if (hasAudioPermission()) {
                startRecording()
            } else {
                requestAudioPermission()
            }
        }

        stopButton.setOnClickListener {
            stopRecording()
        }

        playButton.setOnClickListener {
            playRecording()
        }

        saveButton.setOnClickListener {
            saveRecording()
        }

        cancelButton.setOnClickListener {
            cancelRecording()
        }

        updateUIState(RecorderState.IDLE)
    }

    private fun setupAudioManager() {
        audioManager = AudioManager(this)
        audioManager.setCallback(this)
    }

    private fun checkAudioPermission() {
        if (!hasAudioPermission()) {
            requestAudioPermission()
        }
    }

    private fun hasAudioPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestAudioPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO),
            PERMISSION_REQUEST_RECORD_AUDIO
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        
        when (requestCode) {
            PERMISSION_REQUEST_RECORD_AUDIO -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    Toast.makeText(this, "Permiso concedido", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission denied
                    Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun startRecording() {
        if (!hasAudioPermission()) {
            Toast.makeText(this, getString(R.string.permission_required), Toast.LENGTH_SHORT).show()
            return
        }

        recordingFile = audioManager.startRecording(categoryId)
        if (recordingFile != null) {
            recordingStartTime = System.currentTimeMillis()
            timerHandler.post(timerRunnable)
            updateUIState(RecorderState.RECORDING)
        }
    }

    private fun stopRecording() {
        timerHandler.removeCallbacks(timerRunnable)
        recordingFile = audioManager.stopRecording()
        updateUIState(RecorderState.RECORDED)
    }

    private fun playRecording() {
        recordingFile?.let { file ->
            val message = com.talkingchildren.wearos.models.Message.createRecorded(
                id = file.nameWithoutExtension,
                text = "Mensaje grabado",
                categoryId = categoryId,
                audioFilePath = file.absolutePath
            )
            audioManager.playMessage(message)
        }
    }

    private fun saveRecording() {
        setResult(RESULT_OK)
        finish()
    }

    private fun cancelRecording() {
        // Delete the recording file if it exists
        recordingFile?.let { file ->
            if (file.exists()) {
                file.delete()
            }
        }
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun updateTimer() {
        val elapsed = System.currentTimeMillis() - recordingStartTime
        val seconds = (elapsed / 1000) % 60
        val minutes = (elapsed / 1000) / 60
        timerText.text = String.format("%02d:%02d", minutes, seconds)

        // Auto-stop at max duration
        if (elapsed >= MAX_RECORDING_TIME) {
            stopRecording()
        }
    }

    private fun updateUIState(state: RecorderState) {
        when (state) {
            RecorderState.IDLE -> {
                statusText.text = "Presiona para grabar"
                recordButton.isEnabled = true
                stopButton.isEnabled = false
                playButton.isEnabled = false
                saveButton.isEnabled = false
                timerText.text = "00:00"
            }
            RecorderState.RECORDING -> {
                statusText.text = getString(R.string.recording)
                recordButton.isEnabled = false
                stopButton.isEnabled = true
                playButton.isEnabled = false
                saveButton.isEnabled = false
            }
            RecorderState.RECORDED -> {
                statusText.text = "Grabación completada"
                recordButton.isEnabled = true
                stopButton.isEnabled = false
                playButton.isEnabled = true
                saveButton.isEnabled = true
            }
            RecorderState.PLAYING -> {
                statusText.text = getString(R.string.playing)
                recordButton.isEnabled = false
                stopButton.isEnabled = false
                playButton.isEnabled = false
                saveButton.isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timerHandler.removeCallbacks(timerRunnable)
        audioManager.release()
    }

    // AudioManager callbacks
    override fun onRecordingStarted() {
        runOnUiThread {
            updateUIState(RecorderState.RECORDING)
        }
    }

    override fun onRecordingStopped() {
        runOnUiThread {
            updateUIState(RecorderState.RECORDED)
        }
    }

    override fun onPlaybackStarted() {
        runOnUiThread {
            updateUIState(RecorderState.PLAYING)
        }
    }

    override fun onPlaybackStopped() {
        runOnUiThread {
            updateUIState(RecorderState.RECORDED)
        }
    }

    override fun onError(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            updateUIState(RecorderState.IDLE)
        }
    }

    override fun onTTSReady() {
        // Not used in this activity
    }

    private enum class RecorderState {
        IDLE, RECORDING, RECORDED, PLAYING
    }
}