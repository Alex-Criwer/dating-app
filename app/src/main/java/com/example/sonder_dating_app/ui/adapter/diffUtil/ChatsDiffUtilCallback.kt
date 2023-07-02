package com.example.sonder_dating_app.ui.adapter.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.sonder_domain.models.ChatUser

class ChatsDiffUtilCallback(
    private val oldChats: List<ChatUser>,
    private val newChats: List<ChatUser>,
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldChats.size

    override fun getNewListSize(): Int = newChats.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldChats[oldItemPosition].toUser.uid == newChats[newItemPosition].toUser.uid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldChats[oldItemPosition] == newChats[newItemPosition]
    }
}
