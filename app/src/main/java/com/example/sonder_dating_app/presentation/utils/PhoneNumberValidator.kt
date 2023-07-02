package com.example.sonder_dating_app.presentation.utils

import android.telephony.PhoneNumberUtils

//потом улучшим и et и валидатор
internal class PhoneNumberValidator {

    fun isValid(phoneNumber: String): Boolean {
        return phoneNumber.isNotBlank() && PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber)
    }
}