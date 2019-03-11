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
    var latitude : Double ? = 24.860735
    @get:Exclude
    var longitude : Double ? = 67.001137
    @get:Exclude
    var name: String ? = null
        get() {
            return if (field == null) firstName?.capitalize() + " " + lastName?.capitalize() else field
        }

    var userName: String? = null
    var email: String? = null
    var firstName: String? = null
    var lastName: String? = null
    var licenseNo: String? = null
    var phoneNo: String? = null
    var city : String ? = null
    var type : String ? = null
    var address : String ? = null
    var imageUrl : String ? = ""

    enum class TYPE {LISTER, RENTER}
}