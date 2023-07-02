package com.example.sonder_domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class HobbyType{
    UNKNOWN,
    MATH,
    BOOKS,
    MOVIES,
    PROGRAMMING,
    WALKING,
    DRAWING,
    IDLENESS
}

@Parcelize
data class Hobby(
    val hobbyType: HobbyType = HobbyType.UNKNOWN,
    var value: Int = 50,
    var isSelected: Boolean = false
): Parcelable

fun HobbyType.toHobbyString(): String {
    return when(this) {
        HobbyType.MATH -> "Math"
        HobbyType.BOOKS -> "Books"
        HobbyType.MOVIES -> "Movies"
        HobbyType.PROGRAMMING -> "Programming"
        HobbyType.WALKING -> "Walking"
        HobbyType.DRAWING -> "Drawing"
        HobbyType.IDLENESS -> "Idleness"
        HobbyType.UNKNOWN -> ""
    }
}

fun getHobbiesList(): List<Hobby> {
    return listOf(
        Hobby(hobbyType = HobbyType.MATH), Hobby(hobbyType = HobbyType.BOOKS),
        Hobby(hobbyType = HobbyType.MOVIES), Hobby(hobbyType = HobbyType.PROGRAMMING),
        Hobby(hobbyType = HobbyType.WALKING), Hobby(hobbyType = HobbyType.DRAWING),
        Hobby(hobbyType = HobbyType.IDLENESS)
    )
}
