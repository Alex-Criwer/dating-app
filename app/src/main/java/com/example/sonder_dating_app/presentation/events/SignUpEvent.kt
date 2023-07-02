package com.example.sonder_dating_app.presentation.events

internal sealed interface SignUpEvent : Event {
    object OnSignUpClicked: SignUpEvent
    object OnCreateAccountClicked: SignUpEvent
    class OnSignUpWithPhoneNumberContinueButtonClicked(val phoneNumber: String): SignUpEvent

    object SuccessfulAuthentication: SignUpEvent
    class FailedAuthentication(val message: String): SignUpEvent
    object AuthenticationCodeSend: SignUpEvent

    class VerifyCodeAuthentication(val code: String): SignUpEvent
}
