package com.example.sarwan.renkar.model

import android.net.Uri
import com.example.sarwan.renkar.modules.days.DaysData
import com.google.firebase.Timestamp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.model.value.FieldValue
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

class Cars : Serializable {

    var features : ArrayList<Features> ? = null
    var days : ArrayList<String> ? = DaysData.populateDays().map { it.name } as ArrayList<String>
    var rating : Float ? = null
    var rentedBy : ArrayList<String> ? = null
    var nearestFrom : String ? = null
    @ServerTimestamp
    var createdAt = Timestamp(Date())
    var owner : HashMap<String, Any> = hashMapOf()
    var basic : HashMap<String, Any> = hashMapOf()
    var address : HashMap<String, Any> ?= hashMapOf()
    var price : String ? = null

    @get:Exclude
    var number  : String ? =null

     class Basic : Serializable {
         var name : String ? = null
         var model : String ?  =null
         var description : String ? = ""
         var manufacturedBy : String ? = null
         var miles : String ? = ""
         var coverImagePath : String? = null
    }

    class Specifications : Serializable {
        var fuelType : String ? = "Petrol"
        var capacity : String ? = "4"
        var instantBook : Boolean ? = false
        var delivery : Boolean ? = false
        var color : String ? = "#000000"
        var vehicleType : String ? = ""
    }


    class Owner : Serializable{
        var name : String ? = null
        var email : String ? = null
        var image : String ? = null
    }

     class Registration : Serializable{
        var registeredIn : String ? = Calendar.getInstance().get(Calendar.YEAR).toString()
        var number : String ? = null
    }
}
