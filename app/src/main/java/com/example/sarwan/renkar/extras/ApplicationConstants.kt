package com.example.sarwan.renkar.extras

import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.modules.authentication.LoginActivity

open class ApplicationConstants {
    companion object {
        const val LISTER : String = "LISTER"
        const val SPLASH_TIMEOUT : Long = 3000L
        const val AS_A_LISTER : String = "as a Lister"
        const val AS_A_RENTER : String = "as a Renter"
        val LOGIN_USER_TYPE_TEXT_MAP : HashMap<Boolean, String> = hashMapOf(true to AS_A_LISTER , false to AS_A_RENTER)
        val LOGIN_USER_TYPE_ACTIVITY_MAP : HashMap<Boolean, Class<*>> = hashMapOf(true to LoginActivity::class.java , false to LoginActivity::class.java)
        val SIGNUP_USER_TYPE_LAYOUT_MAP : HashMap<Boolean, Int> = hashMapOf(true to R.layout.signup_lister_layout , false to R.layout.signup_renter_layout)
    }
}