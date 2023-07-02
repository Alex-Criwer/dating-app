package com.example.sonder_domain.mapper

import com.example.sonder_domain.models.GeoCoordinates
import com.example.sonder_domain.models.Media
import com.example.sonder_domain.models.User
import user.User.TUser as GrpcUser
import user.User.TGeo as GrpcGeo
import user.User.TMetaMedia as GrpcMedia
import user.User.EMediaType as GrpcMediaType

class GrpcMapper {
    fun userToGrpcUser(user: User): GrpcUser {
        return GrpcUser.newBuilder().apply {
            with(this) {
                uid = user.uid
                name = user.name
                age = user.age
                gender = user.gender.toGrpcGender()
                lookingForGenderDeprecated = user.lookingForGender.toGrpcGender()
                description = user.description
                interests = user.interests.toGrpcInterests()
                lastGeo = geoToGrpcGeo(user.geo)
                searchDistanceKm = user.searchDistanceKm
                addAllMedia(user.photos.map { photoToGrpcPhoto(it) })
            }
        }.build()
    }

    private fun photoToGrpcPhoto(media: Media): GrpcMedia {
        return GrpcMedia.newBuilder().apply {
            with(this) {
                type = GrpcMediaType.EMT_PHOTO
                path = media.path
            }
        }.build()
    }

    fun geoToGrpcGeo(geo: GeoCoordinates): GrpcGeo {
        return GrpcGeo.newBuilder().apply {
            with(this) {
                latitude = geo.latitude
                longitude = geo.longitude
            }
        }.build()
    }
}
