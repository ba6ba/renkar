package com.example.sarwan.renkar.extras

import java.util.Timer
import java.util.TimerTask
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log

class MyLocation {
    private lateinit  var timer1: Timer
    internal var lm: LocationManager? = null
    private lateinit var locationResult: LocationResult
    internal var gps_enabled = false
    internal var network_enabled = false
    internal var TAG = "MyLocation"

    internal var locationListenerGps: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            timer1.cancel()
            locationResult.gotLocation(location)
            lm?.removeUpdates(this)
            lm?.removeUpdates(locationListenerNetwork)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    internal var locationListenerNetwork: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            timer1.cancel()
            locationResult.gotLocation(location)
            lm?.removeUpdates(this)
            lm?.removeUpdates(locationListenerGps)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    fun getLocation(context: Context, result: LocationResult): Boolean {
        //I use LocationResult callback class to pass location value from MyLocation to user code.
        locationResult = result
        if (lm == null)
            lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //exceptions will be thrown if provider is not permitted.
        try {
            lm?.let { gps_enabled = it.isProviderEnabled(LocationManager.GPS_PROVIDER) }
        } catch (e: SecurityException) {
            Log.d(TAG, e.message)
        }

        try {
            lm?.let { network_enabled = it.isProviderEnabled(LocationManager.NETWORK_PROVIDER) }
        } catch (e: SecurityException) {
            Log.d(TAG, e.message)
        }

        //don't start listeners if no provider is enabled
        if (!gps_enabled && !network_enabled)
            return false

        if (gps_enabled)
            try {
                lm?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListenerGps)
            } catch (e: SecurityException) {
                Log.d(TAG, e.message)
            }

        if (network_enabled)
            try {
                lm?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListenerNetwork)
            } catch (e: SecurityException) {
                Log.d(TAG, e.message)
            }

        timer1 = Timer()
        timer1.schedule(GetLastLocation(), 20000)
        return true
    }

    internal inner class GetLastLocation : TimerTask() {
        override fun run() {
            lm?.removeUpdates(locationListenerGps)
            lm?.removeUpdates(locationListenerNetwork)

            var net_loc: Location? = null
            var gps_loc: Location? = null
            if (gps_enabled)
                try {
                    gps_loc = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                } catch (e: SecurityException) {
                    Log.d(TAG, e.message)
                }

            if (network_enabled)
                try {
                    net_loc = lm?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                } catch (e: SecurityException) {
                    Log.d(TAG, e.message)
                }

            //if there are both values use the latest one
            if (gps_loc != null && net_loc != null) {
                if (gps_loc.time > net_loc.time)
                    locationResult.gotLocation(gps_loc)
                else
                    locationResult.gotLocation(net_loc)
                return
            }

            if (gps_loc != null) {
                locationResult.gotLocation(gps_loc)
                return
            }
            if (net_loc != null) {
                locationResult.gotLocation(net_loc)
                return
            }
            locationResult.gotLocation(null)
        }
    }

    abstract class LocationResult {
        abstract fun gotLocation(location: Location?)
    }
}