package com.example.sarwan.renkar.utils

import android.app.Activity
import android.location.Geocoder
import android.location.Location
import java.util.*
import kotlin.math.roundToLong

class LocationUtility {

    companion object {

        val SOUTH = "south"
        val EAST = "east"
        val CENTRAL = "central"
        val MALIR = "malir"
        val WEST = "west"

        private fun nipa() : Location = Location("").run {
            latitude = 24.917949
            longitude = 67.097024
            return this
        }

        private fun saddar(): Location  = Location("").run {
            latitude = 24.862008
            longitude = 67.030497
            return this
        }

        private fun fiveStar() : Location = Location("").run {
            latitude = 24.986070
            longitude = 67.064002
            return this
        }

        private fun malir() : Location = Location("").run {
            latitude = 24.893137
            longitude = 67.216655
            return this
        }

        private fun metroville() : Location = Location("").run {
            latitude = 24.909453
            longitude = 66.995652
            return this
        }
        var regionsMap = hashMapOf(saddar()to SOUTH ,nipa() to EAST ,fiveStar() to CENTRAL, metroville() to WEST, malir() to MALIR)

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
            var distanceInKiloMeters : Long = 0L
            Location("").apply {
                latitude = userLat
                longitude = userLon
                distanceInKiloMeters = (distanceTo(location)/1000).roundToLong()
            }
            return distanceInKiloMeters
        }

/*
        fun getNearest(lat : Double , lon : Double) : Long?{
            val nearest : ArrayList<Long> = ArrayList()
            val firstLocation = Location("")
            firstLocation.latitude = lat
            firstLocation.longitude = lon
            return regionsMap[regionsMap.keys.minBy { it.distanceTo(firstLocation) }]

        }
*/

        fun getNearest(lat : Double , lon : Double) : String?{
            return regionsMap[regionsMap.keys.minBy { it.distanceTo(Location("").
                apply {
                    latitude = lat ; longitude = lon
                }
            ) } ]
        }

        private fun deg2rad(deg: Double): Double {
            return deg * Math.PI / 180.0
        }

        private fun rad2deg(rad: Double): Double {
            return rad * 180.0 / Math.PI
        }

    }
}