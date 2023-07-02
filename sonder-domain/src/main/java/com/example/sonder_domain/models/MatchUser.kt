package com.example.sonder_domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MatchUser(
    val user: User = User(),
    val reaction: Reaction = Reaction.NOT_SET,
    val isMatch: Boolean = false
) : Parcelable
