package com.example.sarwan.renkar.modules.renter

import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.modules.dashboard.ListerDashboardFragment

class RenterActivity : ParentActivity(){

    private lateinit var dashboardFragment: ListerDashboardFragment
    private lateinit var customTabLayoutFragment: CustomTabLayoutFragment
    private lateinit var topBarFragment: TopBarFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.renter_activity)
        getFragmentsReference()
    }

    private fun getFragmentsReference() {
        dashboardFragment = supportFragmentManager.findFragmentById(R.id.dashboardFragment) as ListerDashboardFragment
        customTabLayoutFragment = supportFragmentManager.findFragmentById(R.id.customTabFragment) as CustomTabLayoutFragment
        topBarFragment = supportFragmentManager.findFragmentById(R.id.top_bar_fragment) as TopBarFragment
        initFragment()
    }

    private fun initFragment() {
        topBarFragment.init(com.example.sarwan.renkar.model.User.TYPE.RENTER.name)
    }
}