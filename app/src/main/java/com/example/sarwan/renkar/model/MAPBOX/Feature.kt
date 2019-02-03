package com.example.sarwan.renkar.model.MAPBOX

import java.io.Serializable


class Feature : Serializable {

    var center: List<Double>? = null
    var context: List<Context>? = null
    var geometry: Geometry? = null
    var id: String? = null
    var place_name: String? = null
    var place_type: List<String>? = null
    var properties: Properties? = null
    var relevance: Double? = null
    var text: String? = null
    var type: String? = null

}
