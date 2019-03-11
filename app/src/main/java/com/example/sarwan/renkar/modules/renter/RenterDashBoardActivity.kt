package com.example.sarwan.renkar.modules.renter

import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.modules.dashboard.ListerDashboardFragment

class RenterDashBoardActivity : ParentActivity() {

    private lateinit var listerDashboardFragment: ListerDashboardFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.renter_dashboard_activity)
        getFragmentReference()
    }

    private fun getFragmentReference(){
        listerDashboardFragment = supportFragmentManager.findFragmentById(R.id.dashboard_fragment) as ListerDashboardFragment
    }

}