package com.example.sarwan.renkar.modules.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sarwan.renkar.model.Cars

class DetailsPagerAdapter(fm: FragmentManager, val car: Cars) : FragmentPagerAdapter(fm) {

    private val noOfPages = 2
    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{ CoverAndMapFragment.newInstance(car)}
            1->{ OverviewFragment.newInstance(car) }
            else -> OverviewFragment.newInstance(car)
        }
    }

    override fun getCount(): Int {
        return noOfPages
    }

}