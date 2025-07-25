package com.talkingchildren.wearos.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.talkingchildren.wearos.R
import com.talkingchildren.wearos.models.Message

/**
 * RecyclerView adapter for displaying messages
 */
class MessageAdapter(
    private val messages: MutableList<Message>,
    private val onPlayMessage: (Message) -> Unit,
    private val onRecordNew: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_MESSAGE = 0
        private const val TYPE_RECORD_NEW = 1
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
        val playButton: ImageButton = itemView.findViewById(R.id.playButton)
    }

    class RecordNewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
        val playButton: ImageButton = itemView.findViewById(R.id.playButton)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == messages.size) TYPE_RECORD_NEW else TYPE_MESSAGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        
        return when (viewType) {
            TYPE_RECORD_NEW -> RecordNewViewHolder(view)
            else -> MessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MessageViewHolder -> {
                val message = messages[position]
                holder.messageText.text = message.text
                holder.playButton.setImageResource(R.drawable.ic_play)
                holder.playButton.setOnClickListener {
                    onPlayMessage(message)
                }
            }
            is RecordNewViewHolder -> {
                holder.messageText.text = holder.itemView.context.getString(R.string.record_new)
                holder.playButton.setImageResource(R.drawable.ic_record)
                holder.itemView.setOnClickListener {
                    onRecordNew()
                }
            }
        }
    }

    override fun getItemCount() = messages.size + 1 // +1 for the "Record New" item

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun removeMessage(message: Message) {
        val index = messages.indexOf(message)
        if (index >= 0) {
            messages.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    fun updateMessages(newMessages: List<Message>) {
        messages.clear()
        messages.addAll(newMessages)
        notifyDataSetChanged()
    }
}