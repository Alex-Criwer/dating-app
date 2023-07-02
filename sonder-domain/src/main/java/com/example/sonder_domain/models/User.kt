package com.example.sonder_domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var uid: String? = null,
    var name: String = "Name",
    var age: Int = 0,
    var gender: Gender = Gender.UNKNOWN,
    var lookingForGender: Gender = Gender.UNKNOWN,
    var description: String = "Description",
    var photos: List<Media> = emptyList(),
    var interests: Interests = Interests(),
    var geo: GeoCoordinates = GeoCoordinates(),
    val searchDistanceKm: Int = 300
) : Parcelable
