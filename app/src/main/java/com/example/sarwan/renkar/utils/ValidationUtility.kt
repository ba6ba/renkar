package com.example.sarwan.renkar.utils

import android.app.Activity
import android.text.TextUtils
import android.widget.TextView
import com.example.sarwan.renkar.R
import java.util.*

class ValidationUtility {
    companion object {
        fun isNotEmptyField(vararg textView: TextView) : Boolean{
            return textView.any {
                return if (TextUtils.isEmpty(it.text)) {
                    it.error="must not be empty"
                    !true
                }else !false
            }
//            return (!textView.any { TextUtils.isEmpty(it.text) })
        }

        fun isValidPassword(textView: TextView) : Boolean{
            return textView.length()==8
        }

        fun isValidEmail(textView: TextView) : Boolean{
            return true
        }

        fun isValidRegex(textView : TextView): Boolean{
            return textView.text.matches(Regex("^[a-zA-Z]*-[\\d]*+\$"))
        }

        fun yearRangeValidation(value: TextView): Boolean {
            return value.text.toString().isNotEmpty().apply {
                if (this){
                    ((value.text.toString().toInt() <= Calendar.getInstance().get(Calendar.YEAR) &&
                            (value.text.toString().toInt() >= Calendar.getInstance().get(Calendar.YEAR)-2000))).run {
                        if (this) true
                        else {
                            value.error = "Car model must be before or by ${Calendar.getInstance().get(Calendar.YEAR)}"
                            false
                        }
                    }
                }}
            }

        fun setErrors(activity : Activity, error: String ,vararg textView: TextView) {
            textView.forEach { it.background = activity.resources.getDrawable(R.drawable.edit_text_background_error) ; it.error = error}
        }
    }
}