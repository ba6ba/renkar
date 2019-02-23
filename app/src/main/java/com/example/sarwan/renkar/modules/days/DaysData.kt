package com.example.sarwan.renkar.modules.days

import com.example.sarwan.renkar.model.Days
import com.example.sarwan.renkar.utils.DateTimeUtility
import java.util.*
import kotlin.collections.ArrayList


class DaysData{
    companion object {

        fun populateDays() : ArrayList<Days>{
            val days :ArrayList<Days> = ArrayList()
            val day1 = Days()
            day1.name = "MONDAY"
            days.add(day1)
            val day2 = Days()
            day2.name = "TUESDAY"
            days.add(day2)
            val day3 = Days()
            day3.name = "WEDNESDAY"
            days.add(day3)
            val day4 = Days()
            day4.name = "THURSDAY"
            days.add(day4)
            val day5 = Days()
            day5.name = "FRIDAY"
            days.add(day5)
            val day6 = Days()
            day6.name = "SATURDAY"
            days.add(day6)
            val day7 = Days()
            day7.name = "SUNDAY"
            days.add(day7)
            return days
        }
    }
}