package com.example.sonder_dating_app.presentation.event_handler

import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.presentation.commands.SignUpCommand.CheckUser
import com.example.sonder_dating_app.presentation.events.SignUpEvent
import com.example.sonder_dating_app.presentation.events.SignUpEvent.*
import com.example.sonder_dating_app.presentation.utils.PhoneNumberValidator
import com.example.sonder_dating_app.presentation.utils.extensions.navigateSafe
import com.example.sonder_dating_app.presentation.view_models.login.SignUpViewModel
import com.example.sonder_dating_app.ui.MainActivity
import kotlinx.coroutines.launch


internal class SignUpEventHandler(
    private val activity: MainActivity,
    private val signUpViewModel: SignUpViewModel,
    private val navController: NavController
) : EventHandler<SignUpEvent>() {
    
    override fun handle(event: SignUpEvent) {
        when(event) {
            OnSignUpClicked -> {
                navController.navigate(R.id.action_signUpFragment_to_signUpWithPhoneNumber)
            }
            OnCreateAccountClicked -> {
                navController.navigate(R.id.action_signUpFragment_to_signUpWithPhoneNumber)
            }
            is OnSignUpWithPhoneNumberContinueButtonClicked -> {
                handleOnContinueSignUpButtonClick(event.phoneNumber)
            }
            is SuccessfulAuthentication -> {
                activity.showBottomNavigation()
                signUpViewModel.dispatchCommand(CheckUser(uid = signUpViewModel.getUid()))
                if (!signUpViewModel.isUserFirstTimeRegisted(uid = signUpViewModel.getUid())) {
                    navController.navigate(R.id.profilesFragment)
                } else navController.navigate(R.id.action_signUpOtpFragment_to_profileFragment)
            }
            AuthenticationCodeSend -> {
                navController.navigateSafe(R.id.action_signUpWithPhoneNumber_to_signUpOtpFragment)
            }
            is FailedAuthentication -> with(activity) {
                showNotification(getString(R.string.sonder_sign_up_with_phone_number_failed) + event.message)
            }
            is VerifyCodeAuthentication -> signUpViewModel.verifyVerificationCode(code = event.code)
        }
    }

    private fun handleOnContinueSignUpButtonClick(phoneNumber: String) {
        if (PhoneNumberValidator().isValid(phoneNumber)) {
            signUpViewModel.verifyPhoneNumber(phoneNumber)
        } else {
            with(activity) {
                showNotification(getString(R.string.sonder_sign_up_with_phone_number_invalid))
            }
        }
    }

}
