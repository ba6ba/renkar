package com.example.sarwan.renkar.modules.details

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.car_details_layout.*

class CarDetailsActivity : ParentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_details_layout)
        setPagerAdapter()
        setBottomSheet()
    }

    private fun setBottomSheet() {
        BottomSheetBehavior.from(bottomSheet).isHideable = false
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        bottomSheet.apply {
            layoutParams.height = displayMetrics.heightPixels
            layoutParams = ViewGroup.LayoutParams(width, height)
        }
    }

    private fun setPagerAdapter() {
        view_pager.adapter = DetailsPagerAdapter(supportFragmentManager)
        fakeDrag()
    }

    private fun fakeDrag() {
        view_pager.apply {
            if (!isFakeDragging)
                fakeDragBy(4F)
        }
    }

    override fun onBackPressed() {
        if(view_pager.currentItem == 0)
            super.onBackPressed()
        else
            view_pager.apply {
                currentItem -= 1
            }
    }
}