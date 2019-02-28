package com.example.sarwan.renkar.modules.details

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.model.Cars
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.car_details_layout.*

class CarDetailsActivity : ParentActivity() {

    private lateinit var car : Cars

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.car_details_layout)
        getIntentData()
        setBottomSheet()
    }

    private fun getIntentData() {
        intent?.extras?.getSerializable(ApplicationConstants.CAR_DETAILS_KEY)?.let {
            car = it as Cars
            setPagerAdapter()
        }
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
        view_pager.adapter = DetailsPagerAdapter(supportFragmentManager, car)
        view_pager.addOnPageChangeListener(pageChangeListener)
        fakeDrag()
    }

    private val pageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {

        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
            changeDots(position)
        }

    }

    private fun changeDots(position: Int) {
        when(position){
            0->{
                dotZero.backgroundTintList = resources.getColorStateList(R.color.light_grey)
                dotOne.backgroundTintList = resources.getColorStateList(R.color.v_light_grey)
            }
            1->{
                dotZero.backgroundTintList = resources.getColorStateList(R.color.v_light_grey)
                dotOne.backgroundTintList = resources.getColorStateList(R.color.light_grey)
            }
        }
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