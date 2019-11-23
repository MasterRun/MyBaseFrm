package com.jsongo.core.util

import android.app.ActivityManager
import android.content.Context
import androidx.fragment.app.FragmentActivity
import com.jsongo.core.BaseCore
import com.safframework.log.L
import java.util.*

/**
 * @author jsongo
 * @date 2018/8/16 8:38
 */
object ActivityCollector {
    private val activities = ArrayList<FragmentActivity>()

    val topActivity: FragmentActivity
        get() = activities[activities.size - 1]

    fun getActivities(): List<FragmentActivity> {
        return activities
    }

    fun addActivity(activity: FragmentActivity) {
        activities.add(activity)
    }

    fun removeActivity(activity: FragmentActivity) {
        activities.remove(activity)
    }

    fun finishAll() {
        for (activity in activities) {
            activity.finish()
        }
        activities.clear()
    }

    fun finish(clazz: Class<out FragmentActivity>) {
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (clazz.isInstance(next)) {
                next.finish()
                iterator.remove()
            }
        }
    }


    fun appExit() {
        try {
            finishAll()
            val activityManager =
                BaseCore.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.restartPackage(BaseCore.context.getPackageName())
            System.exit(0)
        } catch (e: Exception) {
            L.e(e.message, e)
        }
    }
}
