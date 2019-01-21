package com.example.sarwan.renkar.model

import java.io.Serializable

class User : Serializable {

    var listerProfile : ListerProfile ? = null
    get() = ListerProfile()
    var renterProfile : RenterProfile ? = null
    get() = RenterProfile()
    var isLister : Boolean ? = null
    var isLogin : Boolean = false
    var isFirst : Boolean = true
}