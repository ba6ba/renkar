package com.example.sarwan.renkar.model.TPL

import java.util.*

class Locations {

    var compound_address_parents : String ? = null
    var subcat_name : String ? = null
    var name : String ? = null
    var lat : Double ? = null
    var lng : Double ? = null
    var fkey : Long ? = null
    var additional_properties = HashMap<String, Any>()
}

/*
package com.example.sarwan.renkar.model.TPL

import java.io.Serializable

class Locations : Serializable {
    var data : ArrayList<Suggest> ? = null
    class Suggest : Serializable{
        var compound_address_parents : String ? = null
        var subcat_name : String ? = null
        var name : String ? = null
        var lat : Double ? = null
        var lng : Double ? = null
    }
}*/
