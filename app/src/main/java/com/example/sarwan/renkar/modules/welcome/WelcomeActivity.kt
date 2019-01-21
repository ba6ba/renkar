package com.example.sarwan.renkar.modules.welcome

import android.content.Intent
import android.os.Bundle
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.modules.authentication.LoginActivity
import kotlinx.android.synthetic.main.welcome_screen.*

class WelcomeActivity : ParentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.welcome_screen)
        onClickListeners()
    }

    private fun onClickListeners(){
        lister.setOnClickListener {
            openActivity(Intent(this, LoginActivity::class.java).putExtra(ApplicationConstants.LISTER, true))
        }

        renter.setOnClickListener {
            openActivity(Intent(this, LoginActivity::class.java).putExtra(ApplicationConstants.LISTER, false))
        }
    }

}