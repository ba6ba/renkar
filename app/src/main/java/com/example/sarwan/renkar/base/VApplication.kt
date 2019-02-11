package com.example.sarwan.renkar.modules.base

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class VApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeFresco()
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
    }

    private fun initializeFresco() {
        Fresco.initialize(this)
    }
}

