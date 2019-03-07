package com.example.sarwan.renkar.model

import com.google.firebase.firestore.Exclude
import java.io.Serializable
import java.util.*

class PaymentMethods : Serializable {
    var holderName : String ?= null
    var name : String ?= null
    var number : String ?= null
    var ccv : String ?= null
    var expiryDate : String ?= null
    var id = UUID.randomUUID().toString()
    var createdAt  = Calendar.getInstance().time.time
    @get:Exclude
    var active = false
}