package com.example.sarwan.renkar.utils

object RatingUtililty {
    fun getDefaultRating(value : String) : Float{
        return if (value.toInt() < 250) 2.0f else
            if (value.toInt() < 500) 3.0f else
                if (value.toInt() < 750) 4.0f else
                    if (value.toInt() < 1000) 5.0f else 1.0f
    }
}