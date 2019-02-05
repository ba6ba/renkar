package com.example.sarwan.renkar.model

import java.io.Serializable

class Luxuries : Serializable {
    var airConditioned : HashMap<Int, Boolean> = hashMapOf()
    var alloyRim : Boolean ? = null
    var auto : Boolean ? = null
    var convertible  : Boolean ? = null
    var bluetooth  : Boolean ? = null
    var gps  : Boolean ? = null
}