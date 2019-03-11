package com.example.sarwan.renkar.model

import java.io.Serializable

class RenterProfile : Serializable {

    var paymentMethod : ArrayList<PaymentMethods> ? = ArrayList()
    var cars : ArrayList<String> ? = null
}