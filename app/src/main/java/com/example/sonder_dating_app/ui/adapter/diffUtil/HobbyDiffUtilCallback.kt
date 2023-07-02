package com.example.sonder_dating_app.ui.adapter.diffUtil

import androidx.recyclerview.widget.DiffUtil
import com.example.sonder_domain.models.Hobby

class HobbyDiffUtilCallback(
    private val oldHobbies: MutableList<Hobby>,
    private val newHobbies: MutableList<Hobby>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldHobbies.size

    override fun getNewListSize(): Int = newHobbies.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHobbies[oldItemPosition].hobbyType == newHobbies[newItemPosition].hobbyType
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHobbies[oldItemPosition] == newHobbies[newItemPosition]
    }
}
