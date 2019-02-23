package com.example.sarwan.renkar.network


class NetworkConstants {
    companion object {
        //HERE config
        const val HERE_API_VERSION = "/v1"
        const val HERE_PLACES_BASE_URL = "https://places.demo.api.here.com/places"

        //MAP BOX config
        const val MAP_BOX_API_VERSION = "v5/"
        const val MAP_BOX_ACCESS_TOKEN = "pk.eyJ1IjoiYmE2YmEiLCJhIjoiY2pyb293OWtiMTM0dzQ5bXNzY3k2OXdldyJ9.SHjjucqDgGUkogES2nPXLg"
        const val MAP_BOX_PLACES_BASE_URL = "https://api.mapbox.com/geocoding/"

        //TPL config
        const val TPL_MAPS_BASE_URL = "https://api1.tplmaps.com:8888/"
        const val TPL_API_KEY = "$2a$10\$fpXYv71L5pWJ7OuElYfDnD19UHpZq5ndA3Y37K9FtPAnOomNtoSyG"
        const val OUTPUT_KEYS = "compound_address_parents,subcat_name,lat,lng,name,fkey"
    }
}