package com.example.sonder_dating_app.ui.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.CreateMethod.INFLATE
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderHobbyValueBottomSheetDialogBinding
import com.example.sonder_dating_app.presentation.commands.UserProfileCommand.ChangeHobbyValue
import com.example.sonder_dating_app.presentation.view_models.UserProfileViewModel
import com.example.sonder_domain.models.Hobby
import com.example.sonder_domain.models.User
import com.example.sonder_domain.models.toHobbyString
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class HobbySkillValueBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val HOBBY_KEY = "HOBBY_KEY"
        fun newInstance(hobby: Hobby): HobbySkillValueBottomSheet {
            val args = Bundle()
            args.putParcelable(HOBBY_KEY, hobby)
            val fragment = HobbySkillValueBottomSheet()
            fragment.arguments = args
            return fragment
        }
    }

    private val binding: SonderHobbyValueBottomSheetDialogBinding by viewBinding(INFLATE)
    private val userProfileViewModel: UserProfileViewModel by activityViewModels()
    private var hobby: Hobby? = null

    override fun getTheme() = R.style.AppBottomSheetDialogTheme

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        hobby = requireArguments().getParcelable(HOBBY_KEY)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initSubscribe()
    }

    private fun initUi() {
        binding.sonderSkillTitle.text = hobby?.hobbyType?.toHobbyString() ?: getString(R.string.sonder_skill)
        binding.sonderSliderSkill.addOnChangeListener { slider, value, fromUser ->
            binding.sonderSkillValue.text = value.toInt().toString()
        }
    }

    private fun initSubscribe() {
        userProfileViewModel.user.observe(viewLifecycleOwner) { if (it != null) updateUi(it) }
    }

    private fun updateUi(user: User) {
        val interestValue = user.interests.hobbies.find { it.hobbyType == hobby?.hobbyType }?.value?.toFloat()
        if(interestValue != null) binding.sonderSliderSkill.value = interestValue
    }

    override fun dismiss() {
        super.dismiss()
        hobby?.let {  userProfileViewModel.dispatchCommand(
            ChangeHobbyValue(it.copy(value = binding.sonderSliderSkill.value.toInt()))
        )}
    }
}
