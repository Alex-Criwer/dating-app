package com.example.sonder_dating_app.presentation.commands

internal sealed interface ChatsCommand : Command {
    object GetChats: ChatsCommand
}

