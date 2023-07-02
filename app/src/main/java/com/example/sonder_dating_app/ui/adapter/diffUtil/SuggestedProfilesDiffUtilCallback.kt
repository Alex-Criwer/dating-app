package com.example.sonder_dating_app.ui.adapter.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.sonder_domain.models.User

class SuggestedProfilesDiffUtilCallback(
    private val oldUsers: ArrayList<User>,
    private val newUsers: ArrayList<User>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldUsers.size

    override fun getNewListSize(): Int = newUsers.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUsers[oldItemPosition].uid == newUsers[newItemPosition].uid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldUsers[oldItemPosition] == newUsers[newItemPosition]
    }
}
