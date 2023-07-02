package com.example.sonder_dating_app.presentation.event_handler

import androidx.navigation.NavController
import com.example.sonder_dating_app.presentation.commands.ChatCommand.GetLastMessages
import com.example.sonder_dating_app.presentation.commands.ChatCommand.SendMessage
import com.example.sonder_dating_app.presentation.events.ChatEvent
import com.example.sonder_dating_app.presentation.events.ChatEvent.OnInitializeGetLastMessages
import com.example.sonder_dating_app.presentation.events.ChatEvent.OnSendMessageButtonClicked
import com.example.sonder_dating_app.presentation.view_models.chat.ChatViewModel
import com.example.sonder_dating_app.ui.MainActivity

internal class ChatEventHandler(
    private val activity: MainActivity,
    private val chatViewModel: ChatViewModel,
    private val navController: NavController
) : EventHandler<ChatEvent>() {

    override fun handle(event: ChatEvent) {
        when(event) {
            is OnInitializeGetLastMessages -> {
                chatViewModel.dispatchCommand(GetLastMessages(event.fromUid, event.toUid))
            }
            is OnSendMessageButtonClicked -> chatViewModel.dispatchCommand(SendMessage(event.message))
        }
    }
}
