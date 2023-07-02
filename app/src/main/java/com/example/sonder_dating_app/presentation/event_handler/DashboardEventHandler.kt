package com.example.sonder_dating_app.presentation.event_handler

import com.example.sonder_dating_app.presentation.events.*

internal class DashboardEventHandler(
    private val signUpEventHandler: SignUpEventHandler,
    private val userProfileEventHandler: UserProfileEventHandler,
    private val suggestedProfilesEventHandler: SuggestedProfilesEventHandler,
    private val chatsEventHandler: ChatsEventHandler,
    private val chatEventHandler: ChatEventHandler,
    private val matchesEventHandler: MatchesEventHandler
): EventHandler<Event>() {
    override fun handle(event: Event) {
        when(event) {
            is SignUpEvent -> signUpEventHandler.handle(event)
            is UserProfileEvent -> userProfileEventHandler.handle(event)
            is SuggestedProfilesEvent -> suggestedProfilesEventHandler.handle(event)
            is ChatsEvent -> chatsEventHandler.handle(event)
            is ChatEvent -> chatEventHandler.handle(event)
            is MatchesEvent -> matchesEventHandler.handle(event)
        }
    }
}
