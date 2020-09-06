package com.jsongo.core_mini.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.os.Process
import androidx.annotation.Keep

/**
 * @author ： jsongo
 * @date ： 2020/9/6 23:46
 * @desc : 进程工具类
 */
@Keep
object ProcessUtil {

    /**
     * 获取当前进程名
     */
    @JvmStatic
    fun getProcessName(context: Context): String? {
        val pid = Process.myPid()
        try {
            val am = context.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses
            for (procInfo in runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 获取当前运行的进程列表
     */
    @JvmStatic
    fun getRunningProcess(context: Context): MutableList<ActivityManager.RunningAppProcessInfo> =
        try {
            val am = context.getSystemService(Application.ACTIVITY_SERVICE) as ActivityManager
            am.runningAppProcesses
        } catch (e: Exception) {
            e.printStackTrace()
            ArrayList()
        }

    /**
     * 获取进程的后缀名
     */
    @JvmStatic
    fun getProcessSuffixName(
        context: Context,
        processName: String? = getProcessName(context)
    ): String? {
        if (processName.isNullOrEmpty()) {
            return processName
        }
        return processName.substring(context.packageName.length)
    }

    /**
     * 是否是主进程
     */
    @JvmStatic
    fun isMainProcess(
        context: Context,
        processName: String? = getProcessName(context)
    ): Boolean {
        return processName == context.packageName
    }
}