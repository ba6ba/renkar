package com.example.sarwan.renkar.model

import java.io.Serializable
import java.util.*

class Booking : Serializable {
    var listedBy : String ? = null
    var rentedBy : String ? = null
    var carNumber : String ? = null
    var period : String ? = null
    var price : String ? = null
    var id : String = UUID.randomUUID().toString()
    var createdAt = Calendar.getInstance().timeInMillis

}