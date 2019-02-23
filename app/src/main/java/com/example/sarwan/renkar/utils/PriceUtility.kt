package com.example.sarwan.renkar.utils

import java.util.*

class PriceUtility {
    companion object {
        var RENKAR_CUT = 10
        var MINIMUM_DAYS_FOR_RENT = 3
        fun afterRenkarCut(price : String) : String{
            return price.toInt().run {
                this - this.div(RENKAR_CUT)
            }.toString()
        }

        fun weeklyEarning(price: String, count: Int ? = MINIMUM_DAYS_FOR_RENT) : String{
            return "${dailyEarning(price.toInt().times(count?.let { if (it ==0) MINIMUM_DAYS_FOR_RENT else it }?:kotlin.run { 7 }).toString())}"
        }

        fun dailyEarning(price: String) : String{
            return "${afterRenkarCut(price)}/- pkr"
        }
    }
}