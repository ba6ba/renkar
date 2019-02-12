package com.example.sarwan.renkar.model

import android.net.Uri
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
    var registration : Registration ? = Registration()
    var license : License ? = License()
    var listerAmount : String ? = null
    var listedBy : String ? = null
    var rentedBy : ArrayList<String> ? = null
    var nearestKms : Long ? = null
    var coverImagePath : Uri? = null

    inner class Registration {
        var registeredIn : String ? = Calendar.getInstance().get(Calendar.YEAR).toString()
        var registerationNumber : String ? = null
    }

    inner class License {
        var license : String ? = null
        var expiry : String ? = Calendar.getInstance().get(Calendar.YEAR).toString()
    }

}