package com.jsongo.core.util

import android.app.ActivityManager
import android.content.Context
import android.support.v7.app.AppCompatActivity
import com.jsongo.core.BaseCore
import com.safframework.log.L
import java.util.*

/**
 * @author jsongo
 * @date 2018/8/16 8:38
 */
object ActivityCollector {
    private val activities = ArrayList<AppCompatActivity>()

    val topActivity: AppCompatActivity
        get() = activities[activities.size - 1]

    fun getActivities(): List<AppCompatActivity> {
        return activities
    }

    fun addActivity(activity: AppCompatActivity) {
        activities.add(activity)
    }

    fun removeActivity(activity: AppCompatActivity) {
        activities.remove(activity)
    }

    fun finishAll() {
        for (activity in activities) {
            activity.finish()
        }
        activities.clear()
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
