package com.example.sonder_dating_app.ui.fragments.sign_up


import android.os.CountDownTimer
import androidx.activity.OnBackPressedCallback
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderSignUpOtpFragmentBinding
import com.example.sonder_dating_app.firebase.AuthenticationStatus
import com.example.sonder_dating_app.presentation.events.SignUpEvent
import com.example.sonder_dating_app.presentation.events.SignUpEvent.SuccessfulAuthentication
import com.example.sonder_dating_app.presentation.events.SignUpEvent.VerifyCodeAuthentication
import com.example.sonder_dating_app.presentation.utils.extensions.navigateUp
import com.example.sonder_dating_app.presentation.utils.hepler.OTPEditTextHelper

internal class SignUpOtpFragment : BaseSignUpFragment(R.layout.sonder_sign_up_otp_fragment) {

    private val otpHelper = OTPEditTextHelper()
    private lateinit var countDownTimer: CountDownTimer
    private val binding by viewBinding(SonderSignUpOtpFragmentBinding::bind)

    override fun initLoginFragment() {
        initUi()
        initViewModel()
    }

    private fun initUi() {
        setUpTimer()
        binding.sonderOtpSendAgain.setOnClickListener {  }
        otpHelper.bind(
            binding.sonderOtpCode.inputCode1,
            binding.sonderOtpCode.inputCode2,
            binding.sonderOtpCode.inputCode3,
            binding.sonderOtpCode.inputCode4,
            binding.sonderOtpCode.inputCode5,
            binding.sonderOtpCode.inputCode6
        )
        otpHelper.setOnInputOTPComplete { code -> dispatchEvent(VerifyCodeAuthentication(code)) }

        activity?.onBackPressedDispatcher?.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    signUpViewModel.onBack()
                    navigateUp()
                }
        })
    }

    private fun initViewModel() {
        signUpViewModel.status.observe(this) { status ->
            when (status) {
                is AuthenticationStatus.IsSuccessfull -> dispatchEvent(SuccessfulAuthentication)
                is AuthenticationStatus.Exception -> {
                    otpHelper.showVerificationException()
                    dispatchEvent(SignUpEvent.FailedAuthentication(status.message))
                }
                else -> {}
            }
        }
    }

    private fun setUpTimer() {
        countDownTimer = object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (isAdded && view != null) {
                    val seconds = millisUntilFinished / 1000
                    binding.sonderOtpTimer.text = String.format("%02d:%02d", seconds / 60, seconds % 60)
                }
            }
            override fun onFinish() {
                if (isAdded && view != null) {
                    binding.sonderOtpTimer.text = "00:00"
                }
            }
        }
        countDownTimer.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countDownTimer.cancel()
    }
}
