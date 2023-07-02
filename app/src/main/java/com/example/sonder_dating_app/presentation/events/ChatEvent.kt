package com.example.sonder_dating_app.presentation.events

import com.example.sonder_domain.models.Message

internal sealed interface ChatEvent : Event {
    class OnInitializeGetLastMessages(val fromUid: String, val toUid: String): ChatEvent
    class OnSendMessageButtonClicked(val message: Message): ChatEvent
}
