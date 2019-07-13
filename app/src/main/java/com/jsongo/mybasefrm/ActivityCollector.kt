package com.jsongo.mybasefrm

import android.support.v7.app.AppCompatActivity

import java.util.ArrayList

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
}
