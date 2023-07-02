package com.example.sonder_dating_app.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderHobbyItemBinding
import com.example.sonder_domain.models.Hobby
import com.example.sonder_domain.models.getHobbiesList
import com.example.sonder_domain.models.toHobbyString

class HobbyItemViewHolder(
    private val binding: SonderHobbyItemBinding,
    private val onItemClicked: (Int) -> Unit,
    private val onItemLongClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(hobby: Hobby) {
        binding.sonderHobby.text = hobby.hobbyType.toHobbyString()
        if (hobby.isSelected) {
            //binding.sonderHobby.setCardBackgroundColor(R.color.primary_red_transparent)
            binding.sonderHobby.setBackgroundResource(R.drawable.sonder_hobby_shape_selected)
        } else binding.sonderHobby.setBackgroundResource(R.drawable.sonder_hobby_shape_not_selected)
    }

    init {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
        itemView.setOnLongClickListener {
            onItemLongClicked(adapterPosition)
            true
        }
    }
}

class HobbiesAdapter(
    private val onItemClicked: (Hobby) -> Unit,
    private val onItemLongClicked: (Hobby) -> Unit
): RecyclerView.Adapter<HobbyItemViewHolder>() {

    private val _hobbies: MutableList<Hobby> = getHobbiesList().toMutableList()

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(hobbies: MutableList<Hobby>) {
        _hobbies.clear()
        _hobbies.addAll(hobbies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbyItemViewHolder {
        val binding = SonderHobbyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HobbyItemViewHolder(binding, { onItemClicked(_hobbies[it]) }, { onItemLongClicked(_hobbies[it]) })
    }

    override fun getItemCount(): Int = _hobbies.size

    override fun onBindViewHolder(holder: HobbyItemViewHolder, position: Int) {
        holder.bind(_hobbies[position])
    }
}
