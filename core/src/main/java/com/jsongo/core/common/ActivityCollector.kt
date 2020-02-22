package com.jsongo.core.common

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import androidx.fragment.app.FragmentActivity
import com.jsongo.core.BaseCore
import com.jsongo.core.arch.BaseActivity
import com.safframework.log.L


/**
 * @author jsongo
 * @date 2018/8/16 8:38
 */
object ActivityCollector {
    private val activities = ArrayList<FragmentActivity>()

    val topActivity: FragmentActivity
        get() = activities.get(activities.size - 1)

    val myForegroundActivity: FragmentActivity?
        get() {
            return try {
                activities.first { it is BaseActivity && it.isForeground }
            } catch (e: Exception) {
                null
            }
        }

    /**
     * 判断某个Activity 界面是否在前台
     * @param context
     * @param className 某个界面名称
     * @return
     */
    fun isForeground(
        className: String
    ): Boolean {
        if (TextUtils.isEmpty(className)) {
            return false
        }
        val am =
            BaseCore.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn.className) {
                return true
            }
        }
        return false
    }

    val foregroundActivity: FragmentActivity?
        get() {
            for (activity in activities) {
                if (isForeground(activity.componentName.className)) {
                    return activity
                }
            }
            return null
        }

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
