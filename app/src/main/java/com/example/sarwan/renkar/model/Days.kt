package com.example.sarwan.renkar.model

import com.example.sarwan.renkar.utils.StringUtility
import java.io.Serializable

class Days :Serializable {
    var name : String ? = null
    var initial : String ? = StringUtility.makeInitials(name)
    var colorResource : Int ? = null
    var selected : Boolean = false
}