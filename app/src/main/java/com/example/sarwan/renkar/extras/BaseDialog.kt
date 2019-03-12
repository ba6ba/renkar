package com.example.sarwan.renkar.extras

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.example.sarwan.renkar.R

open class BaseDialog : DialogFragment(){

    private val TAG = "BaseDialog"
    override fun show(manager: FragmentManager, tag: String?) {
        if (this.isAdded) {
            return  //or return false/true, based on where you are calling from
        }

        try {
            val ft = manager.beginTransaction()
            ft.add(this, TAG)
            ft.commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState).run {
            window?.attributes?.windowAnimations = R.style.Animation_DialogMessage_Window
            window?.setGravity(Gravity.TOP or Gravity.CENTER_HORIZONTAL)
            window?.attributes?.x = activity?.resources?.getDimensionPixelSize(R.dimen.small_margin)
            window?.attributes?.y = activity?.resources?.getDimensionPixelSize(R.dimen.small_margin)
            return this
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.resources?.let {
            dialog?.window?.setLayout(it.getDimension(R.dimen.dialog_width_large).toInt() , it.getDimension(R.dimen.dialog_height_large).toInt())
            dialog?.window?.setGravity(Gravity.CENTER)
        }
    }
}