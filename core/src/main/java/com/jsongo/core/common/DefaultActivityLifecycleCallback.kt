package com.jsongo.core.common

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * @author ： jsongo
 * @date ： 2020/2/23 9:54
 * @desc : app全局activity声明周期回调
 */
object DefaultActivityLifecycleCallback : Application.ActivityLifecycleCallbacks {

    override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
        ActivityCollector.addActivity(activity)
        ActivityCollector.markActivityFore(activity, true)
    }

    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity?) {

    }

    override fun onActivityResumed(activity: Activity?) {
        ActivityCollector.markActivityFore(activity, true)
    }

    override fun onActivityPaused(activity: Activity?) {
        ActivityCollector.markActivityFore(activity, false)

    }

    override fun onActivityStopped(activity: Activity?) {

    }

    override fun onActivityDestroyed(activity: Activity?) {
        ActivityCollector.removeActivity(activity)
    }

}