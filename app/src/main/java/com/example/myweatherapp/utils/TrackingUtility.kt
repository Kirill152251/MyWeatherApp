package com.example.myweatherapp.utils

import android.content.Context
import pub.devrel.easypermissions.EasyPermissions

object TrackingUtility {
    fun hasLocationPermissions(context: Context) =
        EasyPermissions.hasPermissions(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
}