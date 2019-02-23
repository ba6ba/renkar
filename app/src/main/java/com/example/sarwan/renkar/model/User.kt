package com.example.sarwan.renkar.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

class User : Serializable {

    @get:Exclude
    var lister : ListerProfile ? = null
    get() = ListerProfile()
    @get:Exclude
    var renter : RenterProfile ? = null
    get() = RenterProfile()

    @get:Exclude
    var isLogin : Boolean = false
    @get:Exclude
    var isFirst : Boolean = true

    @get:Exclude
    var password: String? = null
    @get:Exclude
    var latitude : Double ? = null
    @get:Exclude
    var longitude : Double ? = null
    @get:Exclude
    var name: String ? = null
        get() {
            return if (field == null) first_name?.capitalize() + " " + last_name?.capitalize() else field
        }

    var email: String? = null
    var first_name: String? = null
    var last_name: String? = null
    var user_name: String? = null
    var date_of_birth: String? = null
    var phone_no: String? = null
    var city : String ? = null
    var type : String ? = null
    var address : String ? = null
    var image_url : String ? = ""
}