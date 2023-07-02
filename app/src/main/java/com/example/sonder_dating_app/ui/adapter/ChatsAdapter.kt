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
import com.example.sonder_dating_app.databinding.SonderChatsItemViewBinding
import com.example.sonder_dating_app.presentation.utils.hepler.MediaLoader
import com.example.sonder_dating_app.ui.adapter.diffUtil.ChatsDiffUtilCallback
import com.example.sonder_domain.models.ChatUser
import kotlinx.coroutines.launch

class ChatsItemViewHolder(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val mediaLoader: MediaLoader,
    private val binding: SonderChatsItemViewBinding,
    private val onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(chatUser: ChatUser) {
        binding.chatUserName.text = chatUser.toUser.name
        binding.chatUserLastMessage.text = chatUser.lastMessage
        if (chatUser.toUser.photos.isNotEmpty()) {
            lifecycleOwner.lifecycleScope.launch {
                val photo: Bitmap? = mediaLoader.downloadMedia(chatUser.toUser.photos.last())
                if (photo != null) {
                    Glide.with(context).load(photo)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(binding.chatUserImage)
                } else Glide.with(context).load(R.drawable.no_user_image).into(binding.chatUserImage)
            }
        } else {
            Glide.with(context).load(R.drawable.no_user_image).into(binding.chatUserImage)
        }
    }

    init {
        itemView.setOnClickListener { onItemClicked(adapterPosition) }
    }
}

internal class ChatsAdapter(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val mediaLoader: MediaLoader,
    private val onItemClicked: (ChatUser) -> Unit
): RecyclerView.Adapter<ChatsItemViewHolder>() {

    private val _chats: MutableList<ChatUser> = ArrayList()

    fun updateData(chats: List<ChatUser>) {
        val diffCallback = ChatsDiffUtilCallback(_chats, chats)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        _chats.clear()
        _chats.addAll(chats)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsItemViewHolder {
        val binding = SonderChatsItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatsItemViewHolder(context, lifecycleOwner, mediaLoader, binding) {
            onItemClicked(_chats[it])
        }
    }

    override fun getItemCount(): Int = _chats.size

    override fun onBindViewHolder(holder: ChatsItemViewHolder, position: Int) {
        holder.bind(_chats[position])
    }
}
