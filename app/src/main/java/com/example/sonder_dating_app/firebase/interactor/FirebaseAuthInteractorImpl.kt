package com.example.sonder_dating_app.firebase.interactor

import androidx.lifecycle.MutableLiveData
import com.example.sonder_dating_app.firebase.AuthenticationStatus
import com.example.sonder_dating_app.firebase.FirebaseAuthProvider
import com.example.sonder_dating_app.presentation.utils.extensions.default
import com.example.sonder_dating_app.presentation.utils.extensions.set

class FirebaseAuthInteractorImpl(private val auth: FirebaseAuthProvider): FirebaseAuthInteractor {

    private val authenticationStatus =
        MutableLiveData<AuthenticationStatus>().default(AuthenticationStatus.Default)

    init {
        if (auth.isAuthenticate()) authenticationStatus.set(AuthenticationStatus.IsSuccessfull)
        auth.addOnAuthenticationListener(onAuthenticationListener())
    }

    private fun onAuthenticationListener() = object: FirebaseAuthProvider.OnAuthenticationListener {
        override fun onSuccessful() {
            authenticationStatus.set(AuthenticationStatus.IsSuccessfull)
        }

        override fun onCodeSent() {
            authenticationStatus.set(AuthenticationStatus.OnCodeSent(auth.getPhoneNumber()))
        }

        override fun onVerificationFailed(exception: String) {
            authenticationStatus.set(AuthenticationStatus.Exception(exception))
        }
    }

    override fun verifyPhoneNumber(phoneNumber: String) {
        auth.verifyPhoneNumber(phoneNumber)
    }

    override fun resendVerificationCode() {
        auth.resendVerificationCode()
    }

    override fun getAuthenticationStatus(): MutableLiveData<AuthenticationStatus> {
        return authenticationStatus
    }

    override fun verifyVerificationCode(code: String) {
        auth.verifyVerificationCode(code)
    }

    override fun getPhone(): String = auth.getPhoneNumber()

    override fun getUid(): String? = auth.getUid()

    override fun getToken(callback: (String?) -> Unit) = auth.getToken(callback)

    override fun getMessagingToken(callback: (String?) -> Unit) = auth.getMessagingToken(callback)
}
