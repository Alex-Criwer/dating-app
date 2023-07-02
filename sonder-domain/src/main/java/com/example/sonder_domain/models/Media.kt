package com.example.sonder_domain.models

import android.os.Parcelable
import com.google.protobuf.ByteString
import kotlinx.android.parcel.Parcelize

enum class LoadType {
    UNKNOWN,
    FULL,
    BY_PART
}

enum class MediaType {
    UNKNOWN,
    PHOTO,
    VIDEO,
    AUDIO
}

data class LoadingMedia(
    val loadType: LoadType = LoadType.FULL,
    val mediaType: MediaType = MediaType.PHOTO,
    val path: String = "",
    val bytes: ByteString
)

@Parcelize
data class Media(
    val mediaType: MediaType = MediaType.PHOTO,
    val path: String = ""
): Parcelable
