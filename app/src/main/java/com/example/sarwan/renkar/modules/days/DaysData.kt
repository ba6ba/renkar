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
            day1.name = MONDAY
            day1.id = 2
            days.add(day1)
            val day2 = Days()
            day2.name = TUESDAY
            day2.id = 3
            days.add(day2)
            val day3 = Days()
            day3.name = WEDNESDAY
            day3.id = 4
            days.add(day3)
            val day4 = Days()
            day4.name = THURSDAY
            day4.id = 5
            days.add(day4)
            val day5 = Days()
            day5.name = FRIDAY
            day5.id = 6
            days.add(day5)
            val day6 = Days()
            day6.name = SATURDAY
            day6.id = 7
            days.add(day6)
            val day7 = Days()
            day7.name = SUNDAY
            day7.id = 1
            days.add(day7)
            return days
        }

        const val MONDAY = "MONDAY"
        const val TUESDAY = "TUESDAY"
        const val WEDNESDAY = "WEDNESDAY"
        const val THURSDAY = "THURSDAY"
        const val FRIDAY = "FRIDAY"
        const val SATURDAY = "SATURDAY"
        const val SUNDAY = "SUNDAY"
    }
}