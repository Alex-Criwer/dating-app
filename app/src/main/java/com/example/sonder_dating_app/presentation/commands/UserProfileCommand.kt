package com.example.sonder_dating_app.presentation.commands

import com.example.sonder_domain.models.Hobby
import com.example.sonder_domain.models.LoadingMedia
import com.example.sonder_domain.models.User

internal sealed interface UserProfileCommand : Command {
    class GetUserProfileSettings(val uid: String): UserProfileCommand
    class SaveUserProfileSettings(val user: User): UserProfileCommand
    class UploadMedia(val loadingMedia: LoadingMedia): UserProfileCommand
    class ChangeHobbySelectedStatus(val hobby: Hobby): UserProfileCommand
    class ChangeHobbyValue(val hobby: Hobby): UserProfileCommand
}
