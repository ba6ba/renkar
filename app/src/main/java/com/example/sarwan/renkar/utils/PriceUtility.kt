package com.example.sarwan.renkar.utils

class PriceUtility {
    companion object {
        var RENKAR_CUT = 10
        fun afterRenkarCut(price : String) : String{
            return price.toInt().run {
                this - this.div(RENKAR_CUT)
            }.toString()
        }

        fun weeklyEarning(price: String, count: Int?) : String{
            return afterRenkarCut(price).toInt().times(count?.let { it }?:kotlin.run { 7 }).toString()
        }

        fun dailyEarning(price: String) : String{
            return afterRenkarCut(price)
        }
    }
}