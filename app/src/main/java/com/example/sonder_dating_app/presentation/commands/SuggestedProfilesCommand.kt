package com.example.sonder_dating_app.presentation.commands

internal sealed interface SuggestedProfilesCommand : Command {
    object SearchSuggestedUsers: SuggestedProfilesCommand
    object RemoveFirstUser: SuggestedProfilesCommand
    class SetLikeToUser(val fromUid: String, val toUid: String) : SuggestedProfilesCommand
    class SetDislikeToUser(val fromUid: String, val toUid: String) : SuggestedProfilesCommand
}
