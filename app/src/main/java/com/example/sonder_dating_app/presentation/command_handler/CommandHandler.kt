package com.example.sonder_dating_app.presentation.command_handler

internal abstract class CommandHandler<Command: Any> {
    abstract fun handle(command: Command)
}
