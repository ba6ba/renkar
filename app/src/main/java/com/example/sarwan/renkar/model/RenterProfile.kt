package com.example.sarwan.renkar.model

import java.io.Serializable

class RenterProfile : Serializable {

    var cars  : ArrayList<String> ? = null
    var paymentMethod : ArrayList<PaymentMethods> ? = null
}