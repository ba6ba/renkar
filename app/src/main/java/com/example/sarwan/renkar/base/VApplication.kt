package com.example.sarwan.renkar.modules.base

import android.app.Application

class VApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        registerActivityLifecycleCallbacks(AppLifecycleTracker())
    }
}

