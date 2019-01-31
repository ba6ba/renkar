package com.example.sarwan.renkar.modules.days

import com.example.sarwan.renkar.model.Days
import com.example.sarwan.renkar.utils.DateTimeUtility
import java.util.*
import kotlin.collections.ArrayList


class DaysData{
    companion object {

        fun populateDays() : ArrayList<Days>{
            val days :ArrayList<Days> = ArrayList()
            val calendar = Calendar.getInstance()
            calendar.firstDayOfWeek = Calendar.MONDAY
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

            for (i in 0 until 7){
                days[i].name = DateTimeUtility.format.format(calendar.time)
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            return days
        }
    }
}