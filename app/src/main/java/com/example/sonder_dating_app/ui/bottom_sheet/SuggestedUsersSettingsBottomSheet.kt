package com.example.sonder_dating_app.ui.bottom_sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.CreateMethod.INFLATE
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderSuggestedSettingsBottomSheetDialogBinding
import com.example.sonder_dating_app.presentation.commands.SuggestedProfilesCommand
import com.example.sonder_dating_app.presentation.commands.UserProfileCommand.*
import com.example.sonder_dating_app.presentation.utils.extensions.showSafely
import com.example.sonder_dating_app.presentation.view_models.SuggestedProfilesViewModel
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_dating_app.ui.adapter.HobbiesAdapter
import com.example.sonder_domain.models.Gender
import com.example.sonder_domain.models.Interests
import com.example.sonder_domain.models.User
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel.Companion.state as CommonState

@AndroidEntryPoint
internal class SuggestedUsersSettingsBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(): SuggestedUsersSettingsBottomSheet = SuggestedUsersSettingsBottomSheet()
    }

    private val binding: SonderSuggestedSettingsBottomSheetDialogBinding by viewBinding(INFLATE)
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private val suggestedProfilesViewModel: SuggestedProfilesViewModel by activityViewModels()

    private lateinit var hobbiesAdapter: HobbiesAdapter

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {
            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { view ->
                val behaviour = BottomSheetBehavior.from(view)
                setupFullHeight(view)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initSubscribe()
        CommonState.value?.user?.uid?.let { userProfileViewModel.dispatchCommand(GetUserProfileSettings(it)) }
    }

    private fun initUi() {
        hobbiesAdapter = HobbiesAdapter( { hobby ->
            userProfileViewModel.dispatchCommand(ChangeHobbySelectedStatus(hobby))
        },{ hobby ->
            HobbySkillValueBottomSheet.newInstance(hobby)
                .showSafely(requireActivity().supportFragmentManager)
        })
        binding.hobbies.adapter = hobbiesAdapter
        binding.sonderSliderDistance.addOnChangeListener { slider, value, fromUser ->
            binding.sonderDistance.text = value.toInt().toString()
        }
        binding.sonderSliderAge.addOnChangeListener { slider, value, fromUser ->
            val values = binding.sonderSliderAge.values
            binding.sonderAgeRange.text = "${values[0].toInt()}-${values[1].toInt()}"
        }
        binding.saveUserProfileSettings.setOnClickListener {
            getCurrentUserSetting()?.let {
                userProfileViewModel.dispatchCommand(SaveUserProfileSettings(it))
            }
            dismiss()
        }
    }

    private fun initSubscribe() {
        userProfileViewModel.user.observe(viewLifecycleOwner) { if (it != null) updateUi(it) }
    }

    private fun updateUi(user: User) {
        binding.sonderSliderDistance.value = user.searchDistanceKm.toFloat()
        binding.sonderSliderAge.values = listOf(
            user.interests.ageRange.first.toFloat(),
            user.interests.ageRange.second.toFloat()
        )
        setCurrentLookingForGender(user.lookingForGender)
        hobbiesAdapter.updateData(user.interests.hobbies.toMutableList())
    }

    private fun getCurrentUserSetting(): User? {
        val currentUser = userProfileViewModel.user.value
        return currentUser?.copy(
            lookingForGender = getCurrentLookingForGender(),
            searchDistanceKm = binding.sonderSliderDistance.value.toInt(),
            interests = userProfileViewModel.user.value?.interests?.copy(
                ageRange = Pair(
                    binding.sonderSliderAge.values[0].toInt(),
                    binding.sonderSliderAge.values[1].toInt()
                )
            ) ?: Interests()
        )
    }

    private fun getCurrentLookingForGender(): Gender {
        val ids = binding.sonderToggleIntrestedUserGender.checkedButtonIds
        return when {
            ids.singleOrNull() == R.id.sonder_user_man_intrested_gender -> Gender.MALE
            ids.singleOrNull() == R.id.sonder_user_woman_intrested_gender -> Gender.FEMALE
            else -> Gender.UNKNOWN
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

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
        bottomSheet.layoutParams = layoutParams
    }

    override fun dismiss() {
        super.dismiss()
        suggestedProfilesViewModel.dispatchCommand(SuggestedProfilesCommand.SearchSuggestedUsers)
    }
}
