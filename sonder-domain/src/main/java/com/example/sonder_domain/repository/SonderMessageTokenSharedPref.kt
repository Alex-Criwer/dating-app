package com.example.sonder_domain.repository

import android.content.Context

private const val SONDER_TOKEN_SHARED_PREFS = "SONDER_TOKEN_SHARED_PREFS"
private const val TOKEN_KEY = "TOKEN_KEY"

class SonderMessageTokenSharedPref(private val context: Context) {
    private val sharedPref = context.getSharedPreferences(SONDER_TOKEN_SHARED_PREFS, Context.MODE_PRIVATE)

    //возвращает true при первой установке значения
    fun setMessageToken(messageToken: String) : Boolean {
        val isTokenInDBExists: Boolean = sharedPref.getString(TOKEN_KEY, "") != ""
        if (!isTokenInDBExists) {
            sharedPref.edit().putString(TOKEN_KEY, messageToken).apply()
        }
        return !isTokenInDBExists
    }

    fun isUserRegistedFirstTime(): Boolean {
        val isTokenInDBExists: Boolean = sharedPref.getString(TOKEN_KEY, "") != ""
        return !isTokenInDBExists
    }
}
