package com.example.sonder_dating_app.location_manager

import com.example.sonder_domain.models.GeoCoordinates

interface LocationManager {
    suspend fun getLocation() : GeoCoordinates?
}