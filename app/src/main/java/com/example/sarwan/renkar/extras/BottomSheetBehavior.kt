package com.example.sarwan.renkar.extras

import android.app.Activity
import android.graphics.Color
import android.os.Build
import androidx.annotation.AttrRes
import androidx.core.content.ContextCompat
import com.example.sarwan.renkar.R

object BottomSheetBehavior {

     fun setStatusBarDim(activity : Activity, dim : Boolean){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            activity.window.statusBarColor = if (dim) Color.TRANSPARENT else ContextCompat.getColor(activity, getThemedResId(activity,
                R.attr.colorPrimary))
        }
    }

     private fun getThemedResId(activity: Activity, @AttrRes attr : Int) : Int {
        val typedArray = activity.theme.obtainStyledAttributes(intArrayOf(attr))
        val resId = typedArray.getResourceId(0,0)
        typedArray.recycle()
        return resId
    }
}