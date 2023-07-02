package com.example.sonder_dating_app.presentation.view_models.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sonder_dating_app.presentation.commands.ChatsCommand
import com.example.sonder_dating_app.presentation.commands.ChatsCommand.GetChats
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel
import com.example.sonder_domain.mapper.toUser
import com.example.sonder_domain.models.ChatUser
import com.example.sonder_domain.repository.SonderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import misc.Misc
import javax.inject.Inject

@HiltViewModel
internal class ChatsViewModel @Inject constructor(
    private var sonderRepository: SonderRepository
) : BaseViewModel<ChatsCommand>() {

    private val _chats = MutableLiveData<MutableList<ChatUser>>()
    val chats: LiveData<MutableList<ChatUser>> = _chats

    override fun handle(command: ChatsCommand) {
        when(command) {
            GetChats -> getChats()
        }
    }

    private fun getChats() {
        viewModelScope.launch(Dispatchers.IO) {
            val fromUid = state.value?.user?.uid
            val chatsFromResponse = fromUid?.let { sonderRepository.getChats(it) }
            val chats = if (chatsFromResponse != null && chatsFromResponse.status == Misc.EReplyStatus.ERS_OK) {
                chatsFromResponse.chatsList.mapNotNull {
                    val toUid = if (fromUid == it.uiD2) it.uiD1 else it.uiD2
                    val toUser = sonderRepository.getUser(toUid)
                    if (toUser.status == Misc.EReplyStatus.ERS_OK) {
                        ChatUser(toUser = toUser.user.toUser(), lastMessage = it.lastMessage.text)
                    } else null
                 }
            } else emptyList()
            _chats.postValue(chats.toMutableList())
        }
    }
}
