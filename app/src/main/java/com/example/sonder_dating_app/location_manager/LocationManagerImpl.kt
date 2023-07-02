package com.example.sonder_dating_app.location_manager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.sonder_domain.models.GeoCoordinates
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await

internal class LocationManagerImpl(private val context: Context) : LocationManager {

    override suspend fun getLocation(): GeoCoordinates? {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        return if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            try {
                val location = fusedLocationClient.lastLocation.await()
                if (location != null) {
                    GeoCoordinates(location.longitude, location.latitude)
                } else null
            } catch (e: Exception) {
                null
            }
        } else null
    }
}