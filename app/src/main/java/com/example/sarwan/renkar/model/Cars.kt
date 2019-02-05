package com.example.sarwan.renkar.model

import com.example.sarwan.renkar.modules.days.DaysData
import java.io.Serializable
import java.util.*

class Cars : Serializable {
    var name : String ? = null
    var model : String ?  =null
    var description : String ? = ""
    var manufacturedBy : String ? = null
    var vehicleType : String ? = ""
    var miles : String ? = ""
    var fuelType : String ? = "Petrol"
    var features : ArrayList<Features> ? = null
    var capacity : String ? = "4"
    var days : ArrayList<String> ? = DaysData.populateDays().map { it.name } as ArrayList<String>
    var instantBook : Boolean ? = true
    var color : String ? = "#000000"
    var number  : String ? = null
    var rating : Float ? = null
    var registration : Registration ? = null
    var license : License ? = null
    var listerAmount : String ? = null
    var listedBy : String ? = null
    var rentedBy : ArrayList<String> ? = null

    class Registration {
        var registeredIn : String ? = Calendar.getInstance().get(Calendar.YEAR).toString()
        var registerationNumber : String ? = null
    }

    class License {
        var license : String ? = null
        var expiry : String ? = Calendar.getInstance().get(Calendar.YEAR).toString()
    }

}