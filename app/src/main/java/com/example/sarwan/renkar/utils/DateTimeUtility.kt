package com.example.sarwan.renkar.utils

import java.text.SimpleDateFormat
import java.util.*
import android.widget.Spinner
import android.widget.ArrayAdapter
import kotlin.collections.ArrayList


class DateTimeUtility {
    companion object {
        val format = SimpleDateFormat("MM/dd/yyyy")

        fun getYears() : ArrayList<String>{
            val years = ArrayList<String>()
            val thisYear = Calendar.getInstance().get(Calendar.YEAR)
            for (i in 1980..thisYear) {
                years.add(Integer.toString(i))
            }
            return years
        }
    }
}