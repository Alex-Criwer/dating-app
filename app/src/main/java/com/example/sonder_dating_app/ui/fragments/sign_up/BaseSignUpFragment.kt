package com.example.sonder_dating_app.ui.fragments.sign_up

import androidx.fragment.app.viewModels
import com.example.sonder_dating_app.presentation.view_models.login.SignUpViewModel
import com.example.sonder_dating_app.ui.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal abstract class BaseSignUpFragment(layoutId: Int) : BaseFragment(layoutId) {

    protected val signUpViewModel: SignUpViewModel by viewModels()

    override fun initFragment() {
        signUpViewModel.initViewModel()
        initLoginFragment()
    }

    protected open fun initLoginFragment() { }
}