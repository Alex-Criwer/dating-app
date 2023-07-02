package com.example.sonder_domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Interests(
    var hobbies: List<Hobby> = getHobbiesList(),
    var ageRange: Pair<Int, Int> = Pair(18, 100),
): Parcelable
