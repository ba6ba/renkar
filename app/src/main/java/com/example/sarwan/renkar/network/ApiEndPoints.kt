package com.example.sarwan.renkar.network

class ApiEndPoints {
    companion object {
        const val LOCATION = "/location" // exist
        const val PUSH_CAR = "/cars/{id}/push" // E
        const val SUGGEST = "/suggest"
        const val PLACES = "mapbox.places/{query}.json"
        const val SEARCH = "search"
    }
}

