package com.mobitribe.qulabro.modules.authentication

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import com.mobitribe.qulabro.modules.main.MainActivity
import com.mobitribe.qulabro.R
import com.mobitribe.qulabro.extras.ApplicationConstants
import com.mobitribe.qulabro.modules.base.ParentActivity

class SplashActivity : ParentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        openNextScreenInDelay()
    }

    private fun openNextScreenInDelay() {
        Handler().postDelayed({
            profile?.isLogin?.let {
                if (it){
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                        openActivityWithFinish(MainActivity::class.java)
                    else {
                        openActivityWithFinish(PermissionActivity::class.java)
                    }
                } else {
                    openActivityWithFinish(LoginActivity::class.java)
                }
            }

        }, ApplicationConstants.SPLASH_TIMEOUT)
    }
}
