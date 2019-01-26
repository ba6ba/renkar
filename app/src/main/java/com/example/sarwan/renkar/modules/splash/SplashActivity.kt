package com.example.sarwan.renkar.modules.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.sarwan.renkar.R
import com.example.sarwan.renkar.base.ParentActivity
import com.example.sarwan.renkar.extras.ApplicationConstants
import com.example.sarwan.renkar.main.MainActivity
import com.example.sarwan.renkar.modules.authentication.LoginActivity
import com.example.sarwan.renkar.modules.lister.ListerActivity
import com.example.sarwan.renkar.modules.renter.RenterActivity
import com.example.sarwan.renkar.modules.welcome.WelcomeActivity

class SplashActivity : ParentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)
        openNextScreenInDelay()
    }

    private fun openNextScreenInDelay() {
        Handler().postDelayed({
            user?.let {
                when (it.isLogin){
                    true ->{
                        when(it.type){
                            ApplicationConstants.RENTER -> openActivityWithFinish(Intent(this, RenterActivity::class.java))
                            ApplicationConstants.LISTER -> openActivityWithFinish(Intent(this, ListerActivity::class.java))
                        }
                    }
                    false -> if (it.isFirst) openActivityWithFinish(Intent(this, WelcomeActivity::class.java))
                    else openActivityWithFinish(Intent(this, LoginActivity::class.java))
                }
            }?:kotlin.run {
                openActivityWithFinish(Intent(this, WelcomeActivity::class.java))
            }
        }, ApplicationConstants.SPLASH_TIMEOUT)
    }
}
