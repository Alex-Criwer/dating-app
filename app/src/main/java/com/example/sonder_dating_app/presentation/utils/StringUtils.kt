package com.example.sonder_dating_app.presentation.utils

fun String.removeExtraSpaces(): String {
    val trimmedSentence = this.trim()
    val normalizedSentence = trimmedSentence.replace("\\s+".toRegex(), " ")
    return normalizedSentence
}
