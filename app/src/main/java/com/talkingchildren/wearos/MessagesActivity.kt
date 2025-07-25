package com.talkingchildren.wearos

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.talkingchildren.wearos.adapters.MessageAdapter
import com.talkingchildren.wearos.models.Category
import com.talkingchildren.wearos.models.Message

/**
 * Activity displaying messages for a specific category
 */
class MessagesActivity : ComponentActivity(), AudioManager.AudioCallback {

    companion object {
        const val EXTRA_CATEGORY_ID = "category_id"
        const val EXTRA_CATEGORY_NAME = "category_name"
        const val REQUEST_AUDIO_RECORDER = 1001
    }

    private lateinit var messagesRecyclerView: RecyclerView
    private lateinit var categoryTitle: TextView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var audioManager: AudioManager
    private lateinit var categoryId: String
    private val messages = mutableListOf<Message>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        categoryId = intent.getStringExtra(EXTRA_CATEGORY_ID) ?: Category.BASIC
        val categoryName = intent.getStringExtra(EXTRA_CATEGORY_NAME) ?: getString(R.string.category_basic)

        setupViews(categoryName)
        setupAudioManager()
        setupRecyclerView()
        loadMessages()
    }

    private fun setupViews(categoryName: String) {
        categoryTitle = findViewById(R.id.categoryTitle)
        categoryTitle.text = categoryName
    }

    private fun setupAudioManager() {
        audioManager = AudioManager(this)
        audioManager.setCallback(this)
        audioManager.initializeTTS()
    }

    private fun setupRecyclerView() {
        messagesRecyclerView = findViewById(R.id.messagesRecyclerView)
        
        messageAdapter = MessageAdapter(
            messages = messages,
            onPlayMessage = { message ->
                audioManager.playMessage(message)
            },
            onRecordNew = {
                openAudioRecorder()
            }
        )

        messagesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MessagesActivity)
            adapter = messageAdapter
        }
    }

    private fun loadMessages() {
        // Load predefined messages based on category
        val predefinedMessages = getPredefinedMessages(categoryId)
        messages.addAll(predefinedMessages)
        
        // Load recorded messages
        val recordedMessages = audioManager.getRecordedMessages(categoryId)
        messages.addAll(recordedMessages)
        
        messageAdapter.notifyDataSetChanged()
    }

    private fun getPredefinedMessages(categoryId: String): List<Message> {
        return when (categoryId) {
            Category.BASIC -> listOf(
                Message.createPredefined("basic_1", getString(R.string.msg_basic_1), categoryId),
                Message.createPredefined("basic_2", getString(R.string.msg_basic_2), categoryId),
                Message.createPredefined("basic_3", getString(R.string.msg_basic_3), categoryId)
            )
            Category.EMOTIONS -> listOf(
                Message.createPredefined("emotion_1", getString(R.string.msg_emotion_1), categoryId),
                Message.createPredefined("emotion_2", getString(R.string.msg_emotion_2), categoryId),
                Message.createPredefined("emotion_3", getString(R.string.msg_emotion_3), categoryId)
            )
            Category.NEEDS -> listOf(
                Message.createPredefined("need_1", getString(R.string.msg_need_1), categoryId),
                Message.createPredefined("need_2", getString(R.string.msg_need_2), categoryId),
                Message.createPredefined("need_3", getString(R.string.msg_need_3), categoryId)
            )
            else -> emptyList()
        }
    }

    private fun openAudioRecorder() {
        val intent = Intent(this, AudioRecorderActivity::class.java).apply {
            putExtra(AudioRecorderActivity.EXTRA_CATEGORY_ID, categoryId)
        }
        startActivityForResult(intent, REQUEST_AUDIO_RECORDER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == REQUEST_AUDIO_RECORDER && resultCode == RESULT_OK) {
            // Reload messages to include new recorded message
            val newRecordedMessages = audioManager.getRecordedMessages(categoryId)
            
            // Clear existing recorded messages and add new ones
            messages.removeAll { it.type == Message.MessageType.RECORDED }
            messages.addAll(newRecordedMessages)
            
            messageAdapter.notifyDataSetChanged()
            
            Toast.makeText(this, getString(R.string.audio_saved), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        audioManager.release()
    }

    // AudioManager callbacks
    override fun onRecordingStarted() {
        // Not used in this activity
    }

    override fun onRecordingStopped() {
        // Not used in this activity
    }

    override fun onPlaybackStarted() {
        runOnUiThread {
            // Could show visual feedback for playback
        }
    }

    override fun onPlaybackStopped() {
        runOnUiThread {
            // Could hide visual feedback for playback
        }
    }

    override fun onError(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onTTSReady() {
        // TTS is ready for use
    }
}