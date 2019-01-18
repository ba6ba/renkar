package com.example.sarwan.renkar.model

import java.io.Serializable

class ListerProfile : Serializable {

    var email: String? = null
    var id: Int? = null
    var password: String? = null
    var first_name: String? = null
    var last_name: String? = null
    var userName: String ? = null
        get() {
            return if (field == null) first_name?.capitalize() + " " + last_name?.capitalize() else
                field
        }
}




