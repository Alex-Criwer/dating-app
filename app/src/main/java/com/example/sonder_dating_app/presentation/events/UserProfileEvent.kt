package com.example.sonder_dating_app.presentation.events

import com.example.sonder_domain.models.User

internal sealed interface UserProfileEvent : Event {
    class OnInitialize(val uid: String): UserProfileEvent
    class OnSaveProfileSettingsClicked(val user: User): UserProfileEvent
}