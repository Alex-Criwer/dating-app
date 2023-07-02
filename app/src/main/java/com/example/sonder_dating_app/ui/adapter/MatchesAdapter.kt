package com.example.sonder_dating_app.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderMatchesViewBinding
import com.example.sonder_dating_app.presentation.utils.hepler.MediaLoader
import com.example.sonder_dating_app.ui.adapter.diffUtil.MatchesDiffUtilCallback
import com.example.sonder_domain.models.MatchUser
import kotlinx.coroutines.launch

class MatchesItemViewHolder(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val mediaLoader: MediaLoader,
    private val binding: SonderMatchesViewBinding,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(matchUser: MatchUser) {
        binding.matchUserNameAndAge.text = matchUser.user.name
        binding.isMatch.isVisible = matchUser.isMatch

        if (matchUser.user.photos.isNotEmpty()) {
            lifecycleOwner.lifecycleScope.launch {
                val photo: Bitmap? = mediaLoader.downloadMedia(matchUser.user.photos.last())
                if (photo != null) {
                    Glide.with(context).load(photo)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.matchProfilePhoto)
                } else Glide.with(context).load(R.drawable.no_user_image).into(binding.matchProfilePhoto)
            }
        } else {
            Glide.with(context).load(R.drawable.no_user_image).into(binding.matchProfilePhoto)
        }
    }

    init {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }
}

internal class MatchesAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val mediaLoader: MediaLoader,
    private val onItemClicked: (MatchUser) -> Unit
): RecyclerView.Adapter<MatchesItemViewHolder>() {
    private val _users: MutableList<MatchUser> = ArrayList()

    fun updateData(users: MutableList<MatchUser>) {
        Log.d("a", "asca matches users are ${users}")
        val diffCallback = MatchesDiffUtilCallback(_users, users)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        _users.clear()
        _users.addAll(users)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesItemViewHolder {
        val binding = SonderMatchesViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MatchesItemViewHolder(context, lifecycleOwner, mediaLoader, binding) {
            onItemClicked(_users[it])
        }
    }

    override fun getItemCount(): Int = _users.size

    override fun onBindViewHolder(holder: MatchesItemViewHolder, position: Int) {
        holder.bind(_users[position])
    }
}
