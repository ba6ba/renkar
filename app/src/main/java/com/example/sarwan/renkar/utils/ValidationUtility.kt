package com.example.sarwan.renkar.utils

import android.text.TextUtils
import android.widget.TextView

class ValidationUtility {
    companion object {
        fun isNotEmptyField(vararg textView: TextView) : Boolean{
            return !textView.any { TextUtils.isEmpty(it.text) }
        }

        fun isValidPassword(textView: TextView) : Boolean{
            return textView.length()==8
        }

        fun isValidEmail(textView: TextView) : Boolean{
            return true
        }
    }
}