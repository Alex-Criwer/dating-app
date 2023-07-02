package com.example.sonder_dating_app.ui.fragments.sign_up

import android.util.Log
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderSignUpWithPhoneNumberFragmentBinding
import com.example.sonder_dating_app.firebase.AuthenticationStatus
import com.example.sonder_dating_app.presentation.events.SignUpEvent.*

internal class SignUpWithPhoneNumber : BaseSignUpFragment(
    R.layout.sonder_sign_up_with_phone_number_fragment
) {

    private val binding by viewBinding(SonderSignUpWithPhoneNumberFragmentBinding::bind)

    override fun initLoginFragment() {
        initUi()
        initViewModel()
    }

    private fun initUi() {
        binding.sonderEnterPhoneNumber.requestFocus()
        binding.sonderSignUpContinueButton.setOnClickListener(onContinueWithPhoneNumberClicked)
    }

    private fun initViewModel() {
        signUpViewModel.status.observe(this) { status ->
            hideProgressBar()
            when (status) {
                is AuthenticationStatus.IsSuccessfull -> dispatchEvent(SuccessfulAuthentication)
                is AuthenticationStatus.OnCodeSent -> {
                    Log.d("a", "asca code sent")
                    dispatchEvent(AuthenticationCodeSend)
                }
                is AuthenticationStatus.Exception -> {
                    dispatchEvent(FailedAuthentication(status.message))
                }
                else -> {}
            }
        }
    }

    private val onContinueWithPhoneNumberClicked = View.OnClickListener {
        showProgressBar()
        dispatchEvent(
            OnSignUpWithPhoneNumberContinueButtonClicked(
                binding.sonderEnterPhoneNumber.text.toString().trim()
            )
        )
    }

    private fun showProgressBar() {
        binding.sonderSignUpContinueButton.visibility = View.INVISIBLE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.sonderSignUpContinueButton.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }
}
