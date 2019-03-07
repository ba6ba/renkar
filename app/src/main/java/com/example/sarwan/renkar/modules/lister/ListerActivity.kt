package com.example.sarwan.renkar.modules.lister

import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.lister_activity.*

class ListerActivity : ParentActivity(){

    private var navigationPager: ListerNavigationPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lister_activity)
        setTabLayout()
    }

    private fun setTabLayout() {
        navigationPager = ListerNavigationPager(supportFragmentManager)
        viewPager.adapter = navigationPager
        tabLayout.setOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewPager))
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        viewPager.currentItem = 1
    }

    override fun onBackPressed() {
        if (viewPager.currentItem==0)
            super.onBackPressed()
        else
            viewPager.setCurrentItem(0, true)
    }
}