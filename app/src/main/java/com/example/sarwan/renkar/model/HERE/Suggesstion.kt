package com.example.sarwan.renkar.model.HERE

import java.io.Serializable

class Suggesstion : Serializable {

    //free-from text
    var q : String ? = null
    //focal point (co-ordinates)
    var at : String ? = null
    var app_id : String ? = null
    var app_code : String ? = null
}