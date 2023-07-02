package com.example.sonder_domain.mapper

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.example.sonder_domain.models.*
import com.google.protobuf.ByteString
import java.io.ByteArrayOutputStream
import user.User.TUser as GrpcUser
import user.User.TMetaMedia as GrpcMedia
import user.User.TLoadingMedia as GrpcLoadingMedia
import user.User.EMediaType as GrpcMediaType
import user.User.TReaction.EReactionType as GrpcReactionType
import user.User.TMessage as GrpcMessage
import user.User.EGender as GrpcGender
import user.User.TInterests as GrpcInterests
import user.User.TInterests.TInterest as GrpcInterest
import user.User.EInterestType as GrpcInterestType

fun Bitmap.bitmapToByteString(): ByteString {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return ByteString.copyFrom(byteArray)
}

fun ByteString.toBitmap(): Bitmap? {
    val byteArray = this.toByteArray()
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun GrpcUser.toUser(): User {
    return User(
        uid = this.uid,
        name = this.name,
        age =  this.age,
        gender = this.gender.toGender(),
        searchDistanceKm = this.searchDistanceKm,
        lookingForGender = this.lookingForGenderDeprecated.toGender(),
        description = this.description, // TODO this.photosList.map {  it.data.toBitmap() },
        interests = this.interests.toInterests(),
        geo = GeoCoordinates(this.lastGeo.longitude, this.lastGeo.latitude),
        photos = if (this.mediaList.isNotEmpty()) this.mediaList.mapNotNull { it.toPhotos() } else emptyList()
    )
}

fun GrpcMedia.toPhotos(): Media? {
    return if (this.type == GrpcMediaType.EMT_PHOTO) Media(path = this.path) else null
}

fun LoadingMedia.toGrpcLoadingMedia(): GrpcLoadingMedia {
    return GrpcLoadingMedia.newBuilder().apply {
        with(this) {
            type = mediaType.toGrpcMediaType()
            loadType = GrpcLoadingMedia.ELoadType.ELT_FULL
            path = this@toGrpcLoadingMedia.path
            data = bytes
        }
    }.build()
}

fun GrpcLoadingMedia.toLoadingMedia() : LoadingMedia {
    return LoadingMedia(
        path = this.path,
        bytes = this.data
    )
}

fun Media.toGrpcMedia(): GrpcMedia {
    return GrpcMedia.newBuilder().apply {
        with(this) {
            type = mediaType.toGrpcMediaType()
            path = this@toGrpcMedia.path
        }
    }.build()
}

fun MediaType.toGrpcMediaType(): GrpcMediaType {
    return when(this) {
        MediaType.PHOTO -> GrpcMediaType.EMT_PHOTO
        MediaType.AUDIO -> GrpcMediaType.EMT_AUDIO
        MediaType.VIDEO -> GrpcMediaType.EMT_VIDEO
        MediaType.UNKNOWN -> GrpcMediaType.EMT_UNKNOWN
    }
}

fun Reaction.toGrpcReaction(): GrpcReactionType {
    return when(this) {
        Reaction.LIKE -> GrpcReactionType.ERT_LIKE
        Reaction.DISLIKE -> GrpcReactionType.ERT_DISLIKE
        Reaction.NOT_SET -> GrpcReactionType.ERT_UNSET
    }
}

fun GrpcReactionType.toReaction(): Reaction {
    return when(this) {
        GrpcReactionType.ERT_LIKE -> Reaction.LIKE
        GrpcReactionType.ERT_DISLIKE -> Reaction.DISLIKE
        else -> Reaction.NOT_SET
    }
}

fun Message.toGrpcMessage(): GrpcMessage {
    return GrpcMessage.newBuilder().apply {
        with(this) {
            fromUID = this@toGrpcMessage.fromUID
            toUID = this@toGrpcMessage.toUID
            text = this@toGrpcMessage.text
        }
    }.build()
}

fun GrpcMessage.toMessage(): Message {
    return Message(
        fromUID = this.fromUID,
        toUID = this.toUID,
        timestamp = this.timestamp,
        text = this.text
    )
}

fun Gender.toGrpcGender(): GrpcGender {
    return when(this) {
        Gender.MALE -> GrpcGender.EG_MALE
        Gender.FEMALE -> GrpcGender.EG_FEMALE
        Gender.UNKNOWN -> GrpcGender.EG_UNKNOWN
    }
}

fun GrpcGender.toGender(): Gender {
    return when(this) {
        GrpcGender.EG_MALE -> Gender.MALE
        GrpcGender.EG_FEMALE -> Gender.FEMALE
        GrpcGender.EG_UNKNOWN -> Gender.UNKNOWN
        else -> Gender.UNKNOWN
    }
}

fun Interests.toGrpcInterests(): GrpcInterests {
    return GrpcInterests.newBuilder().apply {
        with(this) {
            ageFrom = this@toGrpcInterests.ageRange.first
            ageTo = this@toGrpcInterests.ageRange.second
            addAllInterests(this@toGrpcInterests.hobbies.mapNotNull { if(it.isSelected) it.toGrpcInterest() else null})
        }
    }.build()
}

fun Hobby.toGrpcInterest(): GrpcInterest {
    //if (!this.isSelected) return null
    return GrpcInterest.newBuilder().apply {
        with(this) {
            value = this@toGrpcInterest.value
            type = this@toGrpcInterest.hobbyType.toGrpcInterestType()
        }
    }.build()
}

fun HobbyType.toGrpcInterestType(): GrpcInterestType {
    return when(this) {
        HobbyType.UNKNOWN -> GrpcInterestType.UNRECOGNIZED
        HobbyType.MATH -> GrpcInterestType.EIT_MATH
        HobbyType.BOOKS -> GrpcInterestType.EIT_BOOKS
        HobbyType.MOVIES -> GrpcInterestType.EIT_MOVIES
        HobbyType.PROGRAMMING -> GrpcInterestType.EIT_PROGRAMMING
        HobbyType.WALKING -> GrpcInterestType.EIT_WALKING
        HobbyType.DRAWING -> GrpcInterestType.EIT_DRAWING
        HobbyType.IDLENESS -> GrpcInterestType.EIT_IDLENESS
    }
}

fun GrpcInterests.toInterests(): Interests {
    val selectedHobbies = this.interestsList.map { it.toHobby() }
    val allHobbies = getHobbiesList().map { existingHobby ->
        val matchingHobby = selectedHobbies.firstOrNull { it.hobbyType == existingHobby.hobbyType }
        if (matchingHobby != null) {
            existingHobby.copy(value = matchingHobby.value, isSelected = true)
        } else {
            existingHobby
        }
    }
    return Interests(
        hobbies = allHobbies,
        ageRange = Pair(this.ageFrom, this.ageTo)
    )
}

fun GrpcInterest.toHobby(): Hobby {
    //if (!this.isSelected) return null
    return Hobby(
        value = this.value,
        hobbyType = this.type.toHobbyType(),
        isSelected = true
    )
}

fun GrpcInterestType.toHobbyType(): HobbyType {
    return when(this) {
        GrpcInterestType.UNRECOGNIZED -> HobbyType.UNKNOWN
        GrpcInterestType.EIT_MATH -> HobbyType.MATH
        GrpcInterestType.EIT_BOOKS -> HobbyType.BOOKS
        GrpcInterestType.EIT_MOVIES -> HobbyType.MOVIES
        GrpcInterestType.EIT_PROGRAMMING -> HobbyType.PROGRAMMING
        GrpcInterestType.EIT_WALKING -> HobbyType.WALKING
        GrpcInterestType.EIT_DRAWING -> HobbyType.DRAWING
        GrpcInterestType.EIT_IDLENESS -> HobbyType.IDLENESS
        else -> HobbyType.UNKNOWN
    }
}
