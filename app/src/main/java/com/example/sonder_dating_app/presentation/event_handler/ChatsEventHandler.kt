package com.example.sonder_dating_app.presentation.event_handler

import androidx.navigation.NavController
import com.example.sonder_dating_app.presentation.commands.ChatsCommand.GetChats
import com.example.sonder_dating_app.presentation.events.ChatsEvent
import com.example.sonder_dating_app.presentation.events.ChatsEvent.OnChatUserClicked
import com.example.sonder_dating_app.presentation.events.ChatsEvent.OnInitialize
import com.example.sonder_dating_app.presentation.view_models.chat.ChatsViewModel
import com.example.sonder_dating_app.ui.MainActivity
import com.example.sonder_dating_app.ui.fragments.chat.ChatsFragmentDirections

internal class ChatsEventHandler(
    private val activity: MainActivity,
    private val chatsViewModel: ChatsViewModel,
    private val navController: NavController
) : EventHandler<ChatsEvent>() {

    override fun handle(event: ChatsEvent) {
        when(event) {
            is OnInitialize -> chatsViewModel.dispatchCommand(GetChats)
            is OnChatUserClicked -> {
                navController.navigate(ChatsFragmentDirections.actionChatsFragmentToChatFragment(
                    event.chatUser.toUser
                ))
            }
        }
    }
}
