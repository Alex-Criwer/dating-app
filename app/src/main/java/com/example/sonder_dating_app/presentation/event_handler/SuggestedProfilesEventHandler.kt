package com.example.sonder_dating_app.presentation.event_handler

import com.example.sonder_dating_app.presentation.commands.SuggestedProfilesCommand.SetDislikeToUser
import com.example.sonder_dating_app.presentation.commands.SuggestedProfilesCommand.SetLikeToUser
import com.example.sonder_dating_app.presentation.events.SuggestedProfilesEvent
import com.example.sonder_dating_app.presentation.events.SuggestedProfilesEvent.*
import com.example.sonder_dating_app.presentation.utils.extensions.showSafely
import com.example.sonder_dating_app.presentation.view_models.SuggestedProfilesViewModel
import com.example.sonder_dating_app.ui.MainActivity
import com.example.sonder_dating_app.ui.bottom_sheet.SuggestedUsersSettingsBottomSheet
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

internal class SuggestedProfilesEventHandler (
    private val activity: MainActivity,
    private val suggestedProfilesViewModel: SuggestedProfilesViewModel
) : EventHandler<SuggestedProfilesEvent>() {

    override fun handle(event: SuggestedProfilesEvent) {
        when (event) {
            is OnSetLikeToUserClicked -> {
                suggestedProfilesViewModel.dispatchCommand(SetLikeToUser(event.fromUid, event.toUid))
            }
            is OnSetDislikeToUserClicked -> {
                suggestedProfilesViewModel.dispatchCommand(SetDislikeToUser(event.fromUid, event.toUid))
            }
            is OnSettingsClicked -> {
                SuggestedUsersSettingsBottomSheet.newInstance().showSafely(activity.supportFragmentManager)
            }
        }
    }
}
