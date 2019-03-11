package com.example.sarwan.renkar.base

import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco

class RenkarApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initializeFresco()
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
    }

    private fun initializeFresco() {
        Fresco.initialize(this)
    }
}

