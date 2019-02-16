package com.example.sarwan.renkar.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable

class Features : Serializable {
    @get:Exclude
    var id : Int ? = null
    var name : String ? = null
    @get:Exclude
    var icon : Int ? = null
    @get:Exclude
    var selected : Boolean = false
}