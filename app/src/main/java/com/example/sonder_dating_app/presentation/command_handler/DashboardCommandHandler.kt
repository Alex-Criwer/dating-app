package com.example.sonder_dating_app.presentation.command_handler

import com.example.sonder_dating_app.presentation.commands.*
import com.example.sonder_dating_app.presentation.view_models.MatchesViewModel
import com.example.sonder_dating_app.presentation.view_models.SuggestedProfilesViewModel
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.presentation.view_models.chat.ChatViewModel
import com.example.sonder_dating_app.presentation.view_models.chat.ChatsViewModel
import com.example.sonder_dating_app.presentation.view_models.login.SignUpViewModel

internal class DashboardCommandHandler(
    private val signUpViewModel: SignUpViewModel,
    private val userProfileViewModel: UserProfileViewModel,
    private val suggestedProfilesViewModel: SuggestedProfilesViewModel,
    private val matchesViewModel: MatchesViewModel,
    private val chatsViewModel: ChatsViewModel,
    private val chatViewModel: ChatViewModel
) : CommandHandler<Command>() {
    override fun handle(command: Command) {
        when(command) {
            is SignUpCommand -> signUpViewModel.handle(command)
            is UserProfileCommand -> userProfileViewModel.handle(command)
            is SuggestedProfilesCommand -> suggestedProfilesViewModel.handle(command)
            is MatchesCommand -> matchesViewModel.handle(command)
            is ChatsCommand -> chatsViewModel.handle(command)
            is ChatCommand -> chatViewModel.handle(command)
        }
    }
}
