package com.jsongo.core_mini.util.rxpermissions2

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.util.*

/**
 * @author ： jsongo
 * @date ： 2020/2/22 16:07
 * @desc :
 */
object RxPermissionManager : LifecycleObserver {
    //RxPermissions集合
    private val rxPermissionsCollection = HashMap<Any, RxPermissions>()

    @JvmStatic
    fun get(activity: FragmentActivity): RxPermissions? {
        var rxPermissions = rxPermissionsCollection.get(activity)
        if (rxPermissions == null && !activity.isFinishing) {
            rxPermissions = RxPermissions(activity)
            activity.lifecycle.addObserver(this)
        }
        return rxPermissions
    }

    @JvmStatic
    fun get(fragment: Fragment): RxPermissions? {
        var rxPermissions = rxPermissionsCollection.get(fragment)
        val activity = fragment.activity
        if (rxPermissions == null && activity != null && !activity.isFinishing) {
            rxPermissions = RxPermissions(fragment)
            rxPermissionsCollection[fragment] = rxPermissions
            fragment.lifecycle.addObserver(this)
        }
        return rxPermissions
    }

    //生命周期结束时从集合中移除
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        val iterator = rxPermissionsCollection.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            val key = next.key
            if (key is Activity && key.isFinishing) {
                iterator.remove()
            } else if (key is Fragment) {
                val activity = key.activity
                if (activity == null || activity.isFinishing) {
                    iterator.remove()
                }
            } else {
                iterator.remove()
            }
        }
    }
}