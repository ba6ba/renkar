package com.example.sarwan.renkar.extras

import com.example.sarwan.renkar.R

open class ApplicationConstants {
    companion object {
        const val LISTER : String = "lister"
        const val RENTER : String = "renter"
        const val BOTH : String = "both"
        const val USER_TYPE : String = "USER_TYPE"
        const val SPLASH_TIMEOUT : Long = 3000L
        const val AS_A_LISTER : String = "as a Lister"
        const val AS_A_RENTER : String = "as a Renter"
        val SIGNUP_USER_TYPE_LAYOUT_MAP : HashMap<String, Int> = hashMapOf(ApplicationConstants.LISTER to R.layout.signup_lister_layout , ApplicationConstants.RENTER to R.layout.signup_renter_layout)
    }
}