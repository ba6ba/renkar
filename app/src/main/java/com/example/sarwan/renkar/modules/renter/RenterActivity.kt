package com.example.sarwan.renkar.modules.renter

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.modules.dashboard.ListerDashboardFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.renter_activity.*
import kotlinx.android.synthetic.main.renter_profile_layout.*

class RenterActivity : ParentActivity(){

    private lateinit var customTabLayoutFragment: CustomTabLayoutFragment
    private lateinit var topBarFragment: TopBarFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.renter_activity)
        my_name.text = user?.name
        renter_rides.text = user?.renter?.cars?.count()?.toString() ?: "0"
        setBottomSheet()
        getFragmentsReference()
        clickListener()
    }

    private fun clickListener() {
        dashboard.setOnClickListener {
            attachFragment()
        }
    }

    private fun setBottomSheet() {
        BottomSheetBehavior.from(bottomSheet).apply {
            isHideable = false
            isFitToContents = true
            setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {

                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when(newState){
                        BottomSheetBehavior.STATE_EXPANDED->{
                            com.example.sarwan.renkar.extras.BottomSheetBehavior.setStatusBarDim(this@RenterActivity,true)
                        }
                        BottomSheetBehavior.STATE_HIDDEN->{
                        }
                        BottomSheetBehavior.STATE_COLLAPSED->{
                            com.example.sarwan.renkar.extras.BottomSheetBehavior.setStatusBarDim(this@RenterActivity,false)
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED->{
                            com.example.sarwan.renkar.extras.BottomSheetBehavior.setStatusBarDim(this@RenterActivity,false)
                        }
                        BottomSheetBehavior.STATE_DRAGGING->{}
                        BottomSheetBehavior.STATE_SETTLING->{}
                    }
                }
            })
        }
    }


    private fun attachFragment() {
        BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
        openActivity(Intent(this, RenterDashBoardActivity::class.java))
    }

    private fun getFragmentsReference() {
        customTabLayoutFragment = supportFragmentManager.findFragmentById(R.id.customTabFragment) as CustomTabLayoutFragment
        topBarFragment = supportFragmentManager.findFragmentById(R.id.top_bar_fragment) as TopBarFragment
        initFragment()
    }

    private fun initFragment() {
        topBarFragment.init(com.example.sarwan.renkar.model.User.TYPE.RENTER.name)
    }

    override fun onBackPressed() {
        finish()
    }
}