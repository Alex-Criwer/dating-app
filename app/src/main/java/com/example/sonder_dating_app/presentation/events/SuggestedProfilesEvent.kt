package com.example.sonder_dating_app.presentation.events

internal sealed interface SuggestedProfilesEvent : Event {
    class OnSetLikeToUserClicked(val fromUid: String, val toUid: String): SuggestedProfilesEvent
    class OnSetDislikeToUserClicked(val fromUid: String, val toUid: String): SuggestedProfilesEvent
    object OnSettingsClicked: SuggestedProfilesEvent
}
