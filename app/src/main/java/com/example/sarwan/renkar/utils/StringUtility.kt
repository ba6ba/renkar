package com.example.sarwan.renkar.utils

import android.util.Log

class StringUtility {
    companion object {
        fun makeInitials(text: String?) : String{
            var iconName = ""
            try {
                text?.let {
                    iconName = text[0].toString()
                    if (text.split(" ").size > 1)
                        iconName += text.split(" ")[1][0]
                }
            } catch (e: Exception)
            {
                Log.d("makeInitials", e.message)
            }
            return iconName
        }

        fun getFirstTwoChars(text: String?) : String{
            text?.length?.let {
                    return if (it>2) "${text[0]}${text[1]}" else ""
            }?:kotlin.run { return "" }
        }
    }
}