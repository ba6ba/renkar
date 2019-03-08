package com.example.sarwan.renkar.utils

class BooleanUtility {
    companion object {
        fun toMeaningfulString(b : Boolean?) : String?{
            return b?.let {bool->
                when(bool){
                    true -> {"YES"}
                    false-> {"NO"}
                }
            }?:kotlin.run {
                 null
            }
        }
    }
}