package com.example.sarwan.renkar.model.MAPBOX

import java.io.Serializable


class Addresses : Serializable{

    var attribution: String? = null
    var features: List<Feature>? = null
    var query: List<String>? = null
    var type: String? = null

}
