package com.example.sarwan.renkar.modules.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class DetailsPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {

    private val noOfPages = 2
    override fun getItem(position: Int): Fragment {
        return when(position){
            0->{ CoverAndMapFragment.newInstance()}
            1->{ OverviewFragment.newInstance() }
            else -> OverviewFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return noOfPages
    }

}