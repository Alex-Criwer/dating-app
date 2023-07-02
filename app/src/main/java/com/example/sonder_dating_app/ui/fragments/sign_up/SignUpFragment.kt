package com.example.sonder_dating_app.ui.fragments.sign_up

import android.os.Bundle
import android.view.View
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.sonder_dating_app.R
import com.example.sonder_dating_app.databinding.SonderSignUpFragmentBinding
import com.example.sonder_dating_app.firebase.AuthenticationStatus
import com.example.sonder_dating_app.presentation.events.SignUpEvent.*

internal class SignUpFragment : BaseSignUpFragment(R.layout.sonder_sign_up_fragment) {

    private val binding by viewBinding(SonderSignUpFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        initViewModel()
    }

    private fun initUi() {
        binding.sonderSignUp.setOnClickListener(onSignUpClickListener)
        binding.sonderCreateAccount.setOnClickListener(onCreateAccountClickListener)
    }

    private fun initViewModel() {
        signUpViewModel.status.observe(viewLifecycleOwner) { status ->
            when (status) {
                is AuthenticationStatus.IsSuccessfull -> {
                    dispatchEvent(SuccessfulAuthentication)
                }
                else -> {}
            }
        }
    }

    private val onSignUpClickListener = View.OnClickListener {
        dispatchEvent(OnSignUpClicked)
    }

    private val onCreateAccountClickListener = View.OnClickListener {
        dispatchEvent(OnCreateAccountClicked)
    }
}
