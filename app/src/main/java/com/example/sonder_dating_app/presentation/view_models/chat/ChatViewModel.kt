package com.example.sonder_dating_app.presentation.view_models.chat

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sonder_dating_app.presentation.commands.ChatCommand
import com.example.sonder_dating_app.presentation.commands.ChatCommand.GetLastMessages
import com.example.sonder_dating_app.presentation.commands.ChatCommand.SendMessage
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel
import com.example.sonder_domain.mapper.toMessage
import com.example.sonder_domain.models.Message
import com.example.sonder_domain.repository.SonderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import misc.Misc
import javax.inject.Inject

@HiltViewModel
internal class ChatViewModel @Inject constructor(
    private var sonderRepository: SonderRepository
) : BaseViewModel<ChatCommand>() {

    private val _chatsMessages = MutableLiveData<MutableList<Message>>()
    val chatsMessages: LiveData<MutableList<Message>> = _chatsMessages
    private val _isLoading = MutableLiveData(true)
    val isLoading: LiveData<Boolean> = _isLoading

    override fun handle(command: ChatCommand) {
        when(command) {
            is SendMessage -> sendMessage(command.message)
            is GetLastMessages -> getLastMessages(command.fromUid, command.toUid)
        }
    }

    private fun sendMessage(message: Message) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = sonderRepository.sendMessage(message)
            //TODO сделать обработку состояний и если что пересылать заново
            Log.d("a", "asca send message in before ${_chatsMessages.value}")
            val currentChatsMessages = _chatsMessages.value ?: emptyList()
            val updatedChatsMessages = currentChatsMessages.toMutableList().apply { add(message) }
            _chatsMessages.postValue(updatedChatsMessages)
            Log.d("a", "asca send message in after ${_chatsMessages.value}")
        }
    }

    private fun getLastMessages(fromUid: String, toUid: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val lastMessages = sonderRepository.getLastMessagesRequest(fromUid, toUid)
            val messages = if (lastMessages.status == Misc.EReplyStatus.ERS_OK) {
                _isLoading.postValue(false)
                lastMessages.messagesList.map { it.toMessage() }
            } else emptyList()
            _chatsMessages.postValue(messages.reversed().toMutableList())
        }
    }
}
