package com.example.sonder_dating_app.presentation.events

import com.example.sonder_domain.models.MatchUser

internal sealed interface MatchesEvent : Event {
    class OnMatchUserClicked(val matchUser: MatchUser): MatchesEvent
    class OnInitialize(val uid: String): MatchesEvent
}
