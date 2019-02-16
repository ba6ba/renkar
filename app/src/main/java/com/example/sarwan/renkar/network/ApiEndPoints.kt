package com.example.sarwan.renkar.network

class ApiEndPoints {
    companion object {
        const val SUGGEST = "/suggest"
        const val GROUP_LEAVE = "/groups/{id}/leave" // E
        const val PLACES = "mapbox.places/{query}.json"
        const val SEARCH = "search"
    }
}

