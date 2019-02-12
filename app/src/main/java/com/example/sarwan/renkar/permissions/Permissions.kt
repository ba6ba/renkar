package com.example.sarwan.renkar.permissions

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.model.Features
import kotlin.collections.ArrayList


class Permissions(){
    companion object {
        val permissions : HashMap<String, Int> = hashMapOf(
            android.Manifest.permission.CAMERA to R.drawable.ic_photo_camera,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE to R.drawable.ic_database,
            android.Manifest.permission.ACCESS_COARSE_LOCATION to R.drawable.ic_pin,
            android.Manifest.permission.ACCESS_FINE_LOCATION to R.drawable.ic_pin,
            android.Manifest.permission.CALL_PHONE to R.drawable.ic_phone_receiver
        )

        val permissionsName : HashMap<String, String> = hashMapOf(
            android.Manifest.permission.CAMERA to "CAMERA",
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE to "STORAGE",
            android.Manifest.permission.ACCESS_COARSE_LOCATION to "COARSE LOCATION",
            android.Manifest.permission.ACCESS_FINE_LOCATION to "FINE LOCATION",
            android.Manifest.permission.CALL_PHONE to "PHONE"
        )

        fun getIcon(key : String): Int? {
            return permissions[key]
        }

        fun getName(key : String): String? {
            return permissionsName[key]
        }

        fun getNotGranted(activity: Activity) : ArrayList<String>{
            val list : ArrayList<String> = ArrayList()
            for (permission in permissionsName){
                if(ActivityCompat.checkSelfPermission(activity, permission.key) != PackageManager.PERMISSION_GRANTED){
                    list.add(permission.key)
                }
            }
            return list
        }

        val location_permissions = arrayListOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION)
    }
}