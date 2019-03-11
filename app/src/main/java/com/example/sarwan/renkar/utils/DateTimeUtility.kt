package com.example.sarwan.renkar.utils

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList


class DateTimeUtility {
    companion object {
        val format = SimpleDateFormat("MM/dd/yyyy")

        fun getYears() : ArrayList<String>{
            val years = ArrayList<String>()
            val thisYear = Calendar.getInstance().get(Calendar.YEAR)
            for (i in 2004..thisYear) {
                years.add(Integer.toString(i))
            }
            return years
        }

        fun getNextAvailableDayFromToday(days: java.util.ArrayList<Int>?): String {
            days?.sortedDescending()
            val currentDate = Calendar.getInstance().apply {
                set(Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE))
            }
            while (currentDate[Calendar.DAY_OF_WEEK] != days?.first())
                currentDate.add(Calendar.DATE,1)

            return format.format(currentDate.time)
        }
    }
}