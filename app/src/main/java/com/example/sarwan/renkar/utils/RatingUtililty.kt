package com.example.sarwan.renkar.utils

object RatingUtililty {
    fun getDefaultRating(value : String) : Float{
        return when {
            value.toInt() < 250 -> 2.0f
            value.toInt() < 500 -> 3.0f
            value.toInt() < 750 -> 4.0f
            value.toInt() < 1000 -> 5.0f
            else -> 1.0f
        }
    }
}