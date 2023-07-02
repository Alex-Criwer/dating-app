package com.example.sonder_dating_app.ui.adapter.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.sonder_domain.models.MatchUser

class MatchesDiffUtilCallback(
    private val oldMatches: MutableList<MatchUser>,
    private val newMatches: MutableList<MatchUser>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldMatches.size

    override fun getNewListSize(): Int = newMatches.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMatches[oldItemPosition].user.uid == newMatches[newItemPosition].user.uid
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldMatches[oldItemPosition] == newMatches[newItemPosition]
    }
}
