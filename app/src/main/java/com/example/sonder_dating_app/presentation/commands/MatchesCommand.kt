package com.example.sonder_dating_app.presentation.commands

internal sealed interface MatchesCommand : Command {
    class GetReactionsForUser(val uid: String): MatchesCommand
}
