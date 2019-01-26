package com.example.sarwan.renkar.extras

import android.app.Dialog
import android.content.Intent
import android.widget.*
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.modules.authentication.ListerSignup
import com.example.sarwan.renkar.modules.authentication.RenterSignup
import com.example.sarwan.renkar.modules.authentication.SignupBaseActivity

open class CustomDialogs{

    fun typeDialog(activity: ParentActivity)
    {
        val dialog = Dialog(activity)
        dialog.setContentView(R.layout.select_type)
        // set the custom dialog components - text, image and button
        (dialog.findViewById<RelativeLayout>(R.id.lister)).setOnClickListener {
            activity.openActivity(Intent(activity, ListerSignup::class.java))
        }
        (dialog.findViewById<RelativeLayout>(R.id.renter)).setOnClickListener {
            activity.openActivity(Intent(activity, RenterSignup::class.java))
        }
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()
    }

}