package com.example.sonder_domain.models

import android.graphics.drawable.Drawable
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChatUser(
    var toUser: User = User(),
    var lastMessage: String = ""
) : Parcelable
