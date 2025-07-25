package com.talkingchildren.wearos.models

/**
 * Data class representing a message (predefined or custom recorded)
 */
data class Message(
    val id: String,
    val text: String,
    val categoryId: String,
    val type: MessageType,
    val audioFilePath: String? = null
) {
    enum class MessageType {
        PREDEFINED, // Uses TTS
        RECORDED    // Uses audio file
    }
    
    companion object {
        fun createPredefined(id: String, text: String, categoryId: String): Message {
            return Message(id, text, categoryId, MessageType.PREDEFINED)
        }
        
        fun createRecorded(id: String, text: String, categoryId: String, audioFilePath: String): Message {
            return Message(id, text, categoryId, MessageType.RECORDED, audioFilePath)
        }
    }
}