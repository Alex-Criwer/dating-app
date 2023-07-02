package com.example.sonder_dating_app.ui.fragments.profile

import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderProfileFragmentBinding
import com.example.sonder_dating_app.presentation.commands.SignUpCommand.CheckUser
import com.example.sonder_dating_app.presentation.commands.UserProfileCommand.UploadMedia
import com.example.sonder_dating_app.presentation.events.UserProfileEvent.OnInitialize
import com.example.sonder_dating_app.presentation.events.UserProfileEvent.OnSaveProfileSettingsClicked
import com.example.sonder_dating_app.presentation.utils.MOSCOW
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.presentation.view_models.login.SignUpViewModel
import com.example.sonder_dating_app.ui.fragments.BaseFragment
import com.example.sonder_domain.mapper.bitmapToByteString
import com.example.sonder_domain.models.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel.Companion.state as CommonState

@AndroidEntryPoint
internal class ProfileFragment : BaseFragment(R.layout.sonder_profile_fragment) {

    private val binding by viewBinding(SonderProfileFragmentBinding::bind)
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val signUpViewModel: SignUpViewModel by activityViewModels()

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, uri)
            userProfileViewModel.dispatchCommand(UploadMedia(loadingMedia = LoadingMedia(
                loadType = LoadType.FULL,
                mediaType = MediaType.PHOTO,
                bytes = bitmap.bitmapToByteString()
            )))
            loadImage(bitmap)
        }
    }

    override fun initFragment() {
        initUi()
        initSubscribe()
        CommonState.value?.user?.uid?.let { dispatchEvent(OnInitialize(it)) }
    }

    private fun initUi() {
        binding.saveUserProfileSettings.setOnClickListener(onSaveProfileSettingsClickListener)
        binding.userProfileUserPhoto.setOnClickListener { pickImageLauncher.launch("image/*") }
    }

    private fun initSubscribe() {
        userProfileViewModel.user.distinctUntilChanged().observe(viewLifecycleOwner) {
            if (it != null) updateUi(it) else signUpViewModel.dispatchCommand(CheckUser(uid = signUpViewModel.getUid()))
        }
    }

    private fun updateUi(user: User) {
        binding.userProfileName.setText(user.name)
        binding.userProfileDescription.setText(user.description)
        binding.userAge.setText(user.age.toString())
        setCurrentGender(user.gender)
        setCurrentLookingForGender(user.lookingForGender)
        if (user.photos.isNotEmpty()) {
            viewLifecycleOwner.lifecycleScope.launch {
                loadImage(userProfileViewModel.downloadMedia(user.photos.last()))
            }
        } // пока что одна фотка
    }

    private val onSaveProfileSettingsClickListener = View.OnClickListener {
        dispatchEvent(OnSaveProfileSettingsClicked(getCurrentUserSetting()))
    }

    private fun getCurrentUserSetting(): User {
        val ageString = binding.userAge.text.toString()
        return User(
            uid = CommonState.value?.user?.uid,
            name = binding.userProfileName.text.toString(),
            age = if (ageString.isBlank()) 0 else ageString.toInt(),
            gender = getCurrentGender(),
            lookingForGender = getCurrentLookingForGender(),
            description = binding.userProfileDescription.text.toString(),
            photos = CommonState.value?.user?.photos ?: emptyList(),
            geo = CommonState.value?.user?.geo ?: MOSCOW
        )
    }

    private fun getCurrentGender(): Gender {
        return when(binding.sonderToggleUserGender.checkedButtonId) {
            R.id.sonder_user_man_gender -> Gender.MALE
            R.id.sonder_user_woman_gender -> Gender.FEMALE
            else -> Gender.UNKNOWN
        }
    }

    private fun getCurrentLookingForGender(): Gender {
        val ids = binding.sonderToggleIntrestedUserGender.checkedButtonIds
        return when {
            ids.singleOrNull() == R.id.sonder_user_man_intrested_gender -> Gender.MALE
            ids.singleOrNull() == R.id.sonder_user_woman_intrested_gender -> Gender.FEMALE
            else -> Gender.UNKNOWN
        }
    }

    private fun setCurrentGender(gender: Gender) {
        when(gender) {
            Gender.MALE -> binding.sonderToggleUserGender.check(R.id.sonder_user_man_gender)
            Gender.FEMALE -> binding.sonderToggleUserGender.check(R.id.sonder_user_woman_gender)
            else -> binding.sonderToggleUserGender.clearChecked()
        }
    }

    private fun setCurrentLookingForGender(gender: Gender) {
        when(gender) {
            Gender.MALE -> binding.sonderToggleIntrestedUserGender.check(R.id.sonder_user_man_intrested_gender)
            Gender.FEMALE -> binding.sonderToggleIntrestedUserGender.check(R.id.sonder_user_woman_intrested_gender)
            else -> {
                binding.sonderToggleIntrestedUserGender.check(R.id.sonder_user_man_intrested_gender)
                binding.sonderToggleIntrestedUserGender.check(R.id.sonder_user_woman_intrested_gender)
            }
        }
    }

    private fun loadImage(bitmap: Bitmap?) {
        Log.d("a", "asca load image")
        if (bitmap != null) {
            Glide.with(this).load(bitmap)
                .placeholder(R.drawable.no_user_image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(binding.userProfileUserPhoto)
        }
    }
}
