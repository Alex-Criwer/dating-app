package com.example.sonder_dating_app.presentation.commands

import com.example.sonder_domain.models.Message

internal sealed interface ChatCommand : Command {
    class SendMessage(val message: Message): ChatCommand
    class GetLastMessages(val fromUid: String, val toUid: String): ChatCommand
}
