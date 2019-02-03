package com.example.sarwan.renkar.model.location

import com.google.firebase.firestore.Exclude
import java.io.Serializable

class Address : Serializable {
    @get:Exclude
    var id : Int ? = null
    var address : String ? = null
    var priority : Int ? = id
    var latitude : Double ? = null
    var longitude : Double ? = null
}