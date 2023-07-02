package com.example.sonder_dating_app.presentation.view_models

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sonder_dating_app.presentation.commands.UserProfileCommand
import com.example.sonder_dating_app.presentation.commands.UserProfileCommand.*
import com.example.sonder_dating_app.presentation.utils.extensions.set
import com.example.sonder_dating_app.presentation.utils.hepler.MediaLoader
import com.example.sonder_domain.mapper.toBitmap
import com.example.sonder_domain.mapper.toLoadingMedia
import com.example.sonder_domain.mapper.toPhotos
import com.example.sonder_domain.mapper.toUser
import com.example.sonder_domain.models.Hobby
import com.example.sonder_domain.models.LoadingMedia
import com.example.sonder_domain.models.Media
import com.example.sonder_domain.models.User
import com.example.sonder_domain.repository.SonderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import misc.Misc
import javax.inject.Inject

@HiltViewModel
internal class UserProfileViewModel @Inject constructor(
    private var sonderRepository: SonderRepository
) : BaseViewModel<UserProfileCommand>(), MediaLoader {

    private val errorHandler = CoroutineExceptionHandler { coroutineContext, error ->
        Log.d("a", "asca error is $error")
    }

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    override fun handle(command: UserProfileCommand) {
        when(command) {
            is SaveUserProfileSettings -> handleSaveUserProfileSettings(command.user)
            is GetUserProfileSettings -> getCurrentUser()
            is UploadMedia -> uploadMedia(command.loadingMedia)
            is ChangeHobbySelectedStatus -> changeHobbySelectedStatus(command.hobby)
            is ChangeHobbyValue -> changeHobbyValue(command.hobby)
        }
    }

    private fun getCurrentUser() {
        viewModelScope.launch (errorHandler + Dispatchers.IO) {
            state.value?.user?.uid?.let {
                val grpcUser = sonderRepository.getUser(it)
                val user = if (grpcUser.status == Misc.EReplyStatus.ERS_OK) {
                    grpcUser.user.toUser()
                } else {
                    null
                }
                _user.postValue(user)
            }
        }
    }

    private fun uploadMedia(loadingMedia: LoadingMedia) {
        viewModelScope.launch (errorHandler + Dispatchers.IO) {
            val response = sonderRepository.uploadMedia(loadingMedia)
            if (response.status == Misc.EReplyStatus.ERS_OK) {
                state.value?.user?.let {
                    it.photos = response.media.toPhotos()?.let { listOf(it)} ?: emptyList()
                }
            }
        }
    }

    private fun handleSaveUserProfileSettings(user: User) = viewModelScope.launch(Dispatchers.IO) {
        state.value?.user?.let {
            it.name = user.name
            it.age = user.age
            it.gender = user.gender
            it.lookingForGender = user.lookingForGender
            it.description = user.description
            it.interests = user.interests
            it.photos = user.photos
            it.geo = user.geo
        }
        sonderRepository.updateUser(user)
        _user.postValue(user)
    }

    override suspend fun downloadMedia(media: Media) : Bitmap? {
        val task = viewModelScope.async (errorHandler + Dispatchers.IO) {
            val response = sonderRepository.downloadMedia(media)
            if (response.status == Misc.EReplyStatus.ERS_OK) {
                response.media.toLoadingMedia().bytes.toBitmap()
            } else null
        }
        return task.await()
    }

    private fun changeHobbySelectedStatus(hobby: Hobby) {
        val newUser = _user.value
        newUser?.interests?.hobbies?.find { it.hobbyType == hobby.hobbyType }?.let {
            it.isSelected = !it.isSelected
        }
        _user.set(newUser)
    }

    private fun changeHobbyValue(hobby: Hobby) {
        _user.value?.interests?.hobbies?.find { it.hobbyType == hobby.hobbyType }?.let {
            it.value = hobby.value
        }
    }
}
