package com.example.sonder_dating_app.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.sonder_dating_app.databinding.SonderChatReceivedMessageViewBinding
import com.example.sonder_dating_app.databinding.SonderChatSendMessageViewBinding
import com.example.sonder_dating_app.ui.adapter.diffUtil.ChatDiffUtilCallback
import com.example.sonder_domain.models.Message
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


private fun getDate(timestamp: Long): String {
    val localDateTime = LocalDateTime.ofInstant(
        Instant.ofEpochSecond(timestamp / 1000000000),
        ZoneId.systemDefault()
    )
    return localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
}

internal class ChatSendMessageViewHolder(
    private val binding: SonderChatSendMessageViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind (message: Message) {
        binding.message.text = message.text
        binding.timestamp.text = getDate(message.timestamp)
    }
}

internal class ChatReceivedMessageViewHolder(
    private val binding: SonderChatReceivedMessageViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(message: Message) {
        binding.message.text = message.text
        binding.timestamp.text = getDate(message.timestamp)
    }
}

internal class ChatAdapter(private val senderId: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_SEND: Int = 1
    private val VIEW_TYPE_RECEIVED: Int = 2

    private val _chatsMessages: MutableList<Message> = ArrayList()

    fun updateData(chatsMessages: MutableList<Message>) {
        val diffCallback = ChatDiffUtilCallback(_chatsMessages, chatsMessages)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        _chatsMessages.clear()
        _chatsMessages.addAll(chatsMessages)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemViewType(position: Int): Int {
        return if (_chatsMessages[position].fromUID == senderId) {
            VIEW_TYPE_SEND
        } else VIEW_TYPE_RECEIVED
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_SEND) {
             ChatSendMessageViewHolder(SonderChatSendMessageViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
        } else {
            ChatReceivedMessageViewHolder(SonderChatReceivedMessageViewBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ))
        }
    }

    override fun getItemCount(): Int = _chatsMessages.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_SEND) {
            (holder as ChatSendMessageViewHolder).bind(_chatsMessages[position])
        } else {
            (holder as ChatReceivedMessageViewHolder).bind(_chatsMessages[position])
        }
    }
}
