package com.example.nftb

import android.Manifest
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.content.ContextCompat
import java.lang.Exception


class GpsTracker(private var mContext: Context) : Service(),
    LocationListener {
    var locat: Location? = null
    var latit = 0.0
    var longit = 0.0
    protected var locationManager: LocationManager? = null
    fun getLocation(): Location? {
        try {
            locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager
            val isGPSEnabled = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
            val isNetworkEnabled = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                val hasFineLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
                val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                    hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED
                ) {
                } else return null
                if (isNetworkEnabled) {
                    locationManager!!.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                    if (locationManager != null) {
                        locat =
                            locationManager!!.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                        if (locat != null) {
                            latit = locat!!.latitude
                            longit = locat!!.longitude
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (locat == null) {
                        locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(), this)
                        if (locationManager != null) {
                            locat =
                                locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                            if (locat != null) {
                                latit = locat!!.latitude
                                longit = locat!!.longitude
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.d("@@@", "" + e.toString())
        }
        return locat
    }

    fun getLatitude(): Double {
        if (locat != null) {
            latit = locat!!.latitude
        }
        return latit
    }

    fun getLongitude(): Double {
        if (locat != null) {
            longit = locat!!.longitude
        }
        return longit
    }

    override fun onLocationChanged(location: Location) {}
    override fun onProviderDisabled(provider: String) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onBind(arg0: Intent): IBinder? {
        return null
    }

    fun stopUsingGPS() {
        if (locationManager != null) {
            locationManager!!.removeUpdates(this@GpsTracker)
        }
    }

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10
        private const val MIN_TIME_BW_UPDATES = (1000).toLong()
    }

    init {
        getLocation()
    }
}