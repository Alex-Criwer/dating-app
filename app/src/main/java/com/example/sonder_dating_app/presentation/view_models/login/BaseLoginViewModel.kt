package com.example.sonder_dating_app.presentation.view_models.login

import androidx.lifecycle.MutableLiveData
import com.example.sonder_dating_app.firebase.AuthenticationStatus
import com.example.sonder_dating_app.firebase.interactor.FirebaseAuthInteractor
import com.example.sonder_dating_app.presentation.commands.SignUpCommand
import com.example.sonder_dating_app.presentation.view_models.BaseViewModel

internal abstract class BaseLoginViewModel(
    private var interactor: FirebaseAuthInteractor
): BaseViewModel<SignUpCommand>() {
    lateinit var status: MutableLiveData<AuthenticationStatus>
    val _uid = MutableLiveData<String?>() // нужен чтобы обзервить нормально uid ля видео сервиса

    open fun initViewModel() {
        status = interactor.getAuthenticationStatus()
    }
}
