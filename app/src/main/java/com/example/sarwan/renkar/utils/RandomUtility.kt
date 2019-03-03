package com.example.sarwan.renkar.utils

import com.example.sarwan.renkar.R
import java.util.*
import kotlin.collections.ArrayList

object RandomUtility {

    private fun ClosedRange<Int>.random() =
        Random().nextInt((endInclusive + 1) - start) +  start

    fun darkColors() = ArrayList<Int>().apply {
        add(R.color.grey)
        add(R.color.colorAccent)
        add(R.color.maroon)
        add(R.color.yellow)
        add(R.color.darkest_grey)
    }

    fun getRandomDarkColor() : Int{
        return darkColors()[(0 until darkColors().size -1).random()]
    }
}