package com.example.sonder_dating_app.ui.adapter.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.sonder_domain.models.Message

class ChatDiffUtilCallback(
    private val oldMessages: MutableList<Message>,
    private val newMessages: MutableList<Message>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldMessages.size

    override fun getNewListSize(): Int = newMessages.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMessages[oldItemPosition] == newMessages[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMessages[oldItemPosition] == newMessages[newItemPosition]
    }
}
