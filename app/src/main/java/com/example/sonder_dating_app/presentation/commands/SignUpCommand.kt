package com.example.sonder_dating_app.presentation.commands

internal sealed interface SignUpCommand: Command {
    class CheckUser(val uid: String?): SignUpCommand
}
