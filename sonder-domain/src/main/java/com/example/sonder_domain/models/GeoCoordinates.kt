package com.example.sonder_domain.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GeoCoordinates (
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
) : Parcelable
