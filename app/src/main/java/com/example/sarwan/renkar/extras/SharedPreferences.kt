package com.example.sarwan.renkar.extras

import android.content.Context
import android.preference.PreferenceManager
import com.example.sarwan.renkar.model.ListerProfile
import com.example.sarwan.renkar.model.User

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class SharedPreferences(mContext: Context) {

    private val mPrefs: android.content.SharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext)

    val userProfile: User?
        get() {
            val gson = Gson()
            val json = mPrefs.getString(USER_PROFILE, "")
            val type = object : TypeToken<ListerProfile>() {}.type
            return gson.fromJson(json, type)
        }


    fun saveUserProfile(user: User?): Boolean {
        val prefsEditor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString(USER_PROFILE, json)
        return prefsEditor.commit()
    }

    companion object {
        private const val USER_PROFILE = "USER_PROFILE"
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