package com.example.sonder_dating_app.firebase.interactor

import androidx.lifecycle.MutableLiveData
import com.example.sonder_dating_app.firebase.AuthenticationStatus

interface FirebaseAuthInteractor {
    fun verifyPhoneNumber(phoneNumber: String)
    fun resendVerificationCode()
    fun getAuthenticationStatus(): MutableLiveData<AuthenticationStatus>
    fun verifyVerificationCode(code: String)
    fun getPhone(): String
    fun getUid(): String?
    fun getToken(callback: (String?) -> Unit)
    fun getMessagingToken(callback: (String?) -> Unit)
}
