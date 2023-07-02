package com.example.sonder_dating_app.firebase

sealed interface AuthenticationStatus {
    object Default: AuthenticationStatus
    object IsSuccessfull: AuthenticationStatus
    class OnCodeSent(val phoneNumber: String): AuthenticationStatus
    class Exception(val message: String): AuthenticationStatus
}