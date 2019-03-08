package com.example.sarwan.renkar.model

import java.io.Serializable
import java.util.*

class History : Serializable {
    var id : String = UUID.randomUUID().toString()
    var name : String ? = null
    var rentedBy : String ? = null
    var listedBy : String ? = null
    var details : String ? = null
    var period : String ? = null
    var status : String ? = null
    var createdAt = Calendar.getInstance().time.time
}