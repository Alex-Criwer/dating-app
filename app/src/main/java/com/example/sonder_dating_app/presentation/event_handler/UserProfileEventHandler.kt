package com.example.sonder_dating_app.presentation.event_handler

import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.presentation.commands.UserProfileCommand.GetUserProfileSettings
import com.example.sonder_dating_app.presentation.commands.UserProfileCommand.SaveUserProfileSettings
import com.example.sonder_dating_app.presentation.events.UserProfileEvent
import com.example.sonder_dating_app.presentation.events.UserProfileEvent.OnInitialize
import com.example.sonder_dating_app.presentation.events.UserProfileEvent.OnSaveProfileSettingsClicked
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.ui.MainActivity
import com.example.sonder_domain.models.User

internal class UserProfileEventHandler(
    private val activity: MainActivity,
    private val userProfileViewModel: UserProfileViewModel
) : EventHandler<UserProfileEvent>() {

    override fun handle(event: UserProfileEvent) {
        when(event) {
            is OnInitialize -> userProfileViewModel.dispatchCommand(GetUserProfileSettings(event.uid))
            is OnSaveProfileSettingsClicked -> handleOnSaveProfileSettingsClicked(event.user)
        }
    }

    private fun handleOnSaveProfileSettingsClicked(user: User) {
        with(user) {
            if (name.isNotEmpty() && description.isNotEmpty()) {
                userProfileViewModel.dispatchCommand(SaveUserProfileSettings(user))
            } else {
                with(activity) {
                    showNotification(getString(R.string.sonder_user_profile_invalid_settings))
                }
            }
        }
    }
}
