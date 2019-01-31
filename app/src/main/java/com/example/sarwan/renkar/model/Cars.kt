package com.example.sarwan.renkar.model

import java.io.Serializable

class Cars : Serializable {
    var name : String ? = null
    var model : String ?  =null
    var description : String ? = null
    var manufacturedBy : String ? = null
    var vehicleType : String ? = null
    var fuelType : String ? = null
    var luxuries : Luxuries ? = null
    var capacity : String ? = null
    var instantBook : Boolean ? = null
    var color : String ? = null
    var number  : String ? = null
    var rating : Float ? = null
    var registration : Registration ? = null

    class Registration {
        var registeredIn : String ? = null
        var registerationNumber : String ? = null
    }

    class License {
        var license : String ? = null
        var expiry : String ? = null
    }
}