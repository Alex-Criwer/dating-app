package com.example.sonder_dating_app.ui.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderSuggestedProfileViewBinding
import com.example.sonder_dating_app.presentation.utils.hepler.MediaLoader
import com.example.sonder_dating_app.ui.adapter.diffUtil.SuggestedProfilesDiffUtilCallback
import com.example.sonder_domain.models.User
import kotlinx.coroutines.launch

internal class SuggestedProfilesViewHolder(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val mediaLoader: MediaLoader,
    private val binding: SonderSuggestedProfileViewBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User) {
        binding.userNameAndAge.text = "${user.name}${if (user.age == 0) "" else ", ${user.age}"}"
        binding.userDescription.text = user.description
        if (user.photos.isNotEmpty()) {
            lifecycleOwner.lifecycleScope.launch {
                val photo: Bitmap? = mediaLoader.downloadMedia(user.photos.last())
                if (photo != null) {
                    Glide.with(context).load(photo)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.userProfilePhoto)
                } else Glide.with(context).load(R.drawable.no_user_image)
                    .transition(DrawableTransitionOptions.withCrossFade()).into(binding.userProfilePhoto)
            }
        } else {
            Glide.with(context).load(R.drawable.no_user_image)
                .transition(DrawableTransitionOptions.withCrossFade()).into(binding.userProfilePhoto)
        }
    }
}

internal class SuggestedProfilesAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val mediaLoader: MediaLoader
): RecyclerView.Adapter<SuggestedProfilesViewHolder>() {

    fun setItems(items: ArrayList<User>?) {
        if (items == null) return
        val diffCallback = SuggestedProfilesDiffUtilCallback(users, items)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        users.clear()
        users.addAll(items)
        diffResult.dispatchUpdatesTo(this)
    }

    val users = ArrayList<User>()

    fun getItem(position: Int) = users[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestedProfilesViewHolder {
        val binding = SonderSuggestedProfileViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuggestedProfilesViewHolder(context, lifecycleOwner, mediaLoader, binding)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: SuggestedProfilesViewHolder, position: Int) {
        holder.bind(users[position])
    }
}
