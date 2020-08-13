package com.jsongo.core_mini.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.util.SparseArray
import com.jsongo.core_mini.CoreMini
import com.safframework.log.L

/**
 * @author jsongo
 * @date 2018/8/16 8:38
 */
object ActivityCollector {
    //所有activity集合
    val activities = ArrayList<Activity>()

    //activity 按栈分类
    val taskArray = SparseArray<ArrayList<Activity>>(6)

    val topActivity: Activity
        get() = activities.get(
            activities.size - 1
        )

    val myForegroundActivity: Activity?
        get() {
            return try {
                activities.filter { !it.isFinishing }.last() {
                    isActivityFore(
                        it
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    /**
     * 指定栈的栈顶activity
     */
    fun getTaskTopActivity(taskid: Int) = taskArray[taskid]?.run {
        if (size > 0) {
            get(size - 1)
        } else {
            null
        }
    }

    /**
     * 标记activity在前台，数据放在intent中
     */
    fun markActivityFore(activity: Activity?, isFore: Boolean) {
        activity?.intent?.putExtra(KEY_ACTIVITY_FORE, isFore)
    }

    /**
     * 从intent中获取数据，activity是否在前台
     */
    fun isActivityFore(activity: Activity?) =
        activity?.intent?.getBooleanExtra(KEY_ACTIVITY_FORE, false) ?: false


    /**
     * 判断某个Activity 界面是否在前台
     * @param context
     * @param className 某个界面名称
     * @return
     */
    /*fun isForeground(
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

    val foregroundActivity: Activity?
        get() {
            for (activity in activities) {
                if (isForeground(activity.componentName.className)) {
                    return activity
                }
            }
            return null
        }*/

    fun addActivity(activity: Activity?) {
        if (activity != null) {
            //放入所有activity集合
            activities.add(activity)
            //放入对应task的集合
            val taskId = activity.taskId
            val list = taskArray[taskId] ?: ArrayList()
            list.add(activity)
            taskArray.put(taskId, list)
        }
    }

    fun removeActivity(activity: Activity?) {
        if (activity != null) {
            //从所有activity中移除
            activities.remove(activity)
            //从activity对应栈中移除
            val taskId = activity.taskId
            taskArray[taskId]?.run {
                //移除activity
                remove(activity)
                //集合为空，移除栈
                if (size == 0) {
                    taskArray.remove(taskId)
                }
            }

        }
    }

    fun finishAll() {
        for (activity in activities) {
            activity.finish()
        }
        activities.clear()
        taskArray.clear()
    }

    fun finish(clazz: Class<out Activity>) {
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val next = iterator.next()
            if (clazz.isInstance(next)) {
                //会自动从activitytask集合中移除
                next.finish()
                iterator.remove()
            }
        }
    }


    fun appExit() {
        try {
            finishAll()
            val activityManager =
                CoreMini.context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.restartPackage(CoreMini.context.getPackageName())
            System.exit(0)
        } catch (e: Exception) {
            L.e(e.message, e)
        }
    }

}
