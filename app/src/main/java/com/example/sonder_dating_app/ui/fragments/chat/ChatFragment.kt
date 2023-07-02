package com.example.sonder_dating_app.ui.fragments.chat

import android.graphics.Bitmap
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderChatFragmentBinding
import com.example.sonder_dating_app.presentation.events.ChatEvent.OnInitializeGetLastMessages
import com.example.sonder_dating_app.presentation.events.ChatEvent.OnSendMessageButtonClicked
import com.example.sonder_dating_app.presentation.utils.removeExtraSpaces
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.presentation.view_models.chat.ChatViewModel
import com.example.sonder_dating_app.ui.MainActivity
import com.example.sonder_dating_app.ui.adapter.ChatAdapter
import com.example.sonder_dating_app.ui.fragments.BaseFragment
import com.example.sonder_domain.models.Message
import com.example.sonder_domain.models.User
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Instant
import java.util.Collections.singletonList
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel.Companion.state as CommonState


@AndroidEntryPoint
internal class ChatFragment : BaseFragment(R.layout.sonder_chat_fragment) {
    private lateinit var receiverUser: User
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var chatAdapterObserver: RecyclerView.AdapterDataObserver

    private val binding by viewBinding(SonderChatFragmentBinding::bind)
    private val args: ChatFragmentArgs by navArgs()

    private val chatViewModel: ChatViewModel by activityViewModels()
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()

    override fun initFragment() {
        (activity as MainActivity).hideBottomNavigation()
        receiverUser = args.user
        initUi()
        initSubscribe()
        CommonState.value?.user?.uid?.let {
            dispatchEvent(OnInitializeGetLastMessages(it, receiverUser.uid!!))
        }
    }

    private fun initUi() {
        binding.chatUserName.text = receiverUser.name
        if (receiverUser.photos.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                loadImage(userProfileViewModel.downloadMedia(receiverUser.photos.last()))
            }
        }
        setUpVideoCallButton()
        binding.sendButton.setOnClickListener(onSendMessageClickListener)

        CommonState.value?.user?.uid?.let {
            chatAdapter = ChatAdapter(it)
            chatAdapterObserver = (object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    binding.messagesRv.scrollToPosition(positionStart)
                }
            })
            chatAdapter.registerAdapterDataObserver(chatAdapterObserver)
            binding.messagesRv.adapter = chatAdapter
        }
    }

    private fun initSubscribe() {
        chatViewModel.chatsMessages.observe(viewLifecycleOwner) { messages ->
            chatAdapter.updateData(messages)
        }
        chatViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.isVisible = isLoading
        }
    }

    private val onSendMessageClickListener = View.OnClickListener {
        val fromUid = CommonState.value?.user?.uid
        if (fromUid != null && receiverUser.uid != null && binding.textMessageToSend.text.isNotBlank()) {
            dispatchEvent(OnSendMessageButtonClicked(Message(
                fromUID = fromUid,
                toUID = receiverUser.uid!!,
                timestamp = Instant.now().epochSecond * 1000000000,
                text = binding.textMessageToSend.text.toString().removeExtraSpaces()
            )))
            binding.textMessageToSend.text = null
        }
    }

    private fun setUpVideoCallButton() {
        val fromUid = CommonState.value?.user?.uid
        if (fromUid != null && receiverUser.uid != null) {
            binding.videoCallButton.setIsVideoCall(true)
            binding.videoCallButton.setBackgroundDrawable(resources.getDrawable(R.drawable.sonder_videocam))
            binding.videoCallButton.setResourceID("zego_uikit_call")
            binding.videoCallButton.setInvitees(singletonList(ZegoUIKitUser(receiverUser.uid, receiverUser.name)))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity).showBottomNavigation()
    }

    private fun loadImage(bitmap: Bitmap?) {
        if (bitmap != null) {
            Glide.with(this).load(bitmap).circleCrop().into(binding.chatUserImage)
        }
    }
}
