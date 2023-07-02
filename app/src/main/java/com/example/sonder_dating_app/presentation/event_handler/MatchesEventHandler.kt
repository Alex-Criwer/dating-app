package com.example.sonder_dating_app.presentation.event_handler

import androidx.navigation.NavController
import com.example.sonder_dating_app.presentation.commands.MatchesCommand.GetReactionsForUser
import com.example.sonder_dating_app.presentation.events.MatchesEvent
import com.example.sonder_dating_app.presentation.events.MatchesEvent.OnInitialize
import com.example.sonder_dating_app.presentation.events.MatchesEvent.OnMatchUserClicked
import com.example.sonder_dating_app.presentation.view_models.MatchesViewModel
import com.example.sonder_dating_app.ui.fragments.likes.MatchesFragmentDirections

internal class MatchesEventHandler(
    private val matchesViewModel: MatchesViewModel,
    private val navController: NavController
) : EventHandler<MatchesEvent>() {

    override fun handle(event: MatchesEvent) {
        when (event) {
            is OnInitialize -> matchesViewModel.dispatchCommand(GetReactionsForUser(event.uid))
            is OnMatchUserClicked -> {
                navController.navigate(MatchesFragmentDirections.actionLikesFragmentToChatFragment(
                    event.matchUser.user
                ))
            }
        }
    }
}
