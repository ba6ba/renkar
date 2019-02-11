package com.example.sarwan.renkar.utils

import android.app.Activity
import android.location.Geocoder
import android.location.Location
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.roundToLong

class LocationUtility {

    companion object {
        fun getAddress(activity: Activity , lat : Double, lon : Double) : String {
            val gcd = Geocoder(activity, Locale.getDefault())
            val addresses = gcd.getFromLocation(lat, lon, 1)
            return addresses[0].locality
        }

        fun calculateDistance(userLat : Double, userLon : Double , lat : Double,lon : Double): Long {
            val firstLocation = Location("")
            firstLocation.latitude = userLat
            firstLocation.longitude = userLon

            val secondLocation = Location("")
            secondLocation.latitude = lat
            secondLocation.longitude = lon
            val distanceInKiloMeters = (firstLocation.distanceTo(secondLocation))/1000
            return distanceInKiloMeters.roundToLong()
        }

        fun calculateDistance(userLat : Double, userLon : Double, location: Location): Long {
            val firstLocation = Location("")
            firstLocation.latitude = userLat
            firstLocation.longitude = userLon
            val distanceInKiloMeters = (firstLocation.distanceTo(location))/1000
            return distanceInKiloMeters.roundToLong()
        }

        fun getNearest(lat : Double , lon : Double) : Long?{
            val nearest : ArrayList<Long> = ArrayList()
            for (i in popularLocations){
                nearest.add(calculateDistance(lat, lon, i))
            }
            return nearest.min()
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }

        private var popularLocations : ArrayList<Location> = arrayListOf(NIPA(), SADDAR(), POWERHOUSE() , MALIR())

        private fun NIPA() : Location = Location("").run {
            latitude = 24.917949
            longitude = 67.097024
            return this
        }

        private fun SADDAR(): Location  = Location("").run {
            latitude = 24.862008
            longitude = 67.030497
            return this
        }

        private fun POWERHOUSE() : Location = Location("").run {
            latitude = 24.986070
            longitude = 67.064002
            return this
        }

        private fun MALIR() : Location = Location("").run {
            latitude = 24.893137
            longitude = 67.216655
            return this
        }
    }
}