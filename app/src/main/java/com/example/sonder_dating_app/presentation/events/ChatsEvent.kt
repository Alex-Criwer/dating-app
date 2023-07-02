package com.example.sonder_dating_app.presentation.events

import com.example.sonder_domain.models.ChatUser

internal sealed interface ChatsEvent : Event {
    object OnInitialize: ChatsEvent
    class OnChatUserClicked(val chatUser: ChatUser): ChatsEvent
}
