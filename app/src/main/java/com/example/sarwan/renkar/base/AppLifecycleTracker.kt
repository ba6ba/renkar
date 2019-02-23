package com.example.sarwan.renkar.modules.base

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.example.sarwan.renkar.base.ParentActivity

class AppLifecycleTracker : Application.ActivityLifecycleCallbacks  {
  override fun onActivityPaused(activity: Activity?) {

  }

  override fun onActivityResumed(activity: Activity?) {
  }

  override fun onActivityDestroyed(activity: Activity?) {
  }

  override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
  }

  override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
  }

  private var numStarted = 0

  override fun onActivityStarted(activity: Activity?) {
    if (numStarted == 0) {
      // app went to foreground
      //(activity as? ParentActivity)?.goOnline()
    }
    numStarted++
  }

  override fun onActivityStopped(activity: Activity?) {
    numStarted--
    if (numStarted == 0) {
      //(activity as? ParentActivity)?.goOffline()
    }
  }

}