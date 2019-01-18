package com.example.sarwan.renkar.extras

import android.content.Context
import android.preference.PreferenceManager
import com.example.sarwan.renkar.model.ListerProfile
import com.example.sarwan.renkar.model.RenterProfile

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.HashMap


class SharedPreferences(mContext: Context) {

    private val mPrefs: android.content.SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    val listerProfile: ListerProfile?
        get() {
            val gson = Gson()
            val json = mPrefs.getString(LISTER_PROFILE, "")
            val type = object : TypeToken<ListerProfile>() {}.type
            return gson.fromJson(json, type)
        }

    val renterProfile: RenterProfile?
        get() {
            val gson = Gson()
            val json = mPrefs.getString(RENTER_PROFILE, "")
            val type = object : TypeToken<ListerProfile>() {}.type
            return gson.fromJson(json, type)
        }


    fun saveListerProfile(listerProfile: ListerProfile?): Boolean {
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(listerProfile)
        prefsEditor.putString(LISTER_PROFILE, json)
        return prefsEditor.commit()
    }

    fun saveRenterProfile(renterProfile: RenterProfile?): Boolean {
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(renterProfile)
        prefsEditor.putString(LISTER_PROFILE, json)
        return prefsEditor.commit()
    }

    companion object {
        private const val LISTER_PROFILE = "LISTER_PROFILE"
        private const val RENTER_PROFILE = "RENTER_PROFILE"
        private const val FIREBASE_TOKEN = "FIREBASE_TOKEN"
    }
    fun getFirebaseToken(): String {
        return mPrefs.getString(FIREBASE_TOKEN, "")!!
    }

    fun saveFirebaseToken(token: String) {
        val prefsEditor = mPrefs.edit()
        prefsEditor.putString(FIREBASE_TOKEN, token)
        prefsEditor.apply()
    }
}