package com.example.sarwan.renkar.modules.lister

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.sarwan.renkar.model.User
import com.example.sarwan.renkar.modules.dashboard.ListerDashboardFragment
import com.example.sarwan.renkar.modules.lister.add.ListerAddCarFragment
import com.example.sarwan.renkar.modules.lister.listing.ListerCarsFragment

class ListerNavigationPager(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private val NUM_ITEMS = 3

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return when (position) {
            0 -> ListerCarsFragment.newInstance()
            1 -> ListerAddCarFragment.newInstance()
            else -> ListerDashboardFragment.newInstance(User.TYPE.LISTER.ordinal)
        }
    }

    override fun getCount(): Int {
        return NUM_ITEMS
    }

}