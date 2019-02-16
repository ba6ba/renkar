package com.example.sarwan.renkar.modules.location

import android.app.Activity
import android.location.Location
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.MyLocation
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.permissions.Permissions
import com.example.sarwan.renkar.utils.LocationUtility

class Location(val activity : ParentActivity) {

    private var count = 0

     fun checkForLocationPermissions()  : Int{
        for (locationPermission in Permissions.location_permissions){
            if(!Permissions.getNotGranted(activity).contains(locationPermission)){
                count+=1
            }
        }
         return count
    }

    fun get() {
        MyLocation().getLocation(activity, locationResult)
    }

    private var locationResult: MyLocation.LocationResult = object : MyLocation.LocationResult() {
        override fun gotLocation(location: Location?) {
            //Got the location!
            location?.let {
                activity.user?.latitude = location.latitude
                activity.user?.longitude = location.longitude
                activity.user?.address = LocationUtility.getAddress(activity, location.latitude, location.longitude)
                activity.saveUserInSharedPreferences()
            }
        }
    }

}