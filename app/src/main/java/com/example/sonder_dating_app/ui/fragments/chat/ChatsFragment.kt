package com.example.sonder_dating_app.ui.fragments.chat

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderChatsFragmentBinding
import com.example.sonder_dating_app.presentation.events.ChatsEvent.OnChatUserClicked
import com.example.sonder_dating_app.presentation.events.ChatsEvent.OnInitialize
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.presentation.view_models.chat.ChatsViewModel
import com.example.sonder_dating_app.ui.adapter.ChatsAdapter
import com.example.sonder_dating_app.ui.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class ChatsFragment : BaseFragment(R.layout.sonder_chats_fragment){

    private val binding by viewBinding(SonderChatsFragmentBinding::bind)

    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val chatsViewModel: ChatsViewModel by activityViewModels()

    private lateinit var chatsAdapter: ChatsAdapter

    override fun initFragment() {
        initUi()
        initSubscribe()
        dispatchEvent(OnInitialize)
    }

    private fun initUi() {
        chatsAdapter = ChatsAdapter(requireContext(), viewLifecycleOwner, userProfileViewModel) { chatUser ->
            dispatchEvent(OnChatUserClicked(chatUser))
        }
        binding.chatsList.apply {
            adapter = chatsAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initSubscribe() {
        chatsViewModel.chats.observe(viewLifecycleOwner) {
            chatsAdapter.updateData(it)
        }
    }
}
