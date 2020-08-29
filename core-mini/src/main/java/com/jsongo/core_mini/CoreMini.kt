package com.jsongo.core_mini

import android.app.ActivityManager
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import android.os.Process
import androidx.annotation.Keep
import androidx.multidex.MultiDex
import com.bumptech.glide.Glide
import com.jsongo.core_mini.common.DefaultActivityLifecycleCallback
import com.jsongo.core_mini.widget.imagepreview.ZoomImageLoader
import com.previewlibrary.ZoomMediaLoader

/**
 * @author ： jsongo
 * @date ： 2020/7/26 16:56
 * @desc :
 */
open class CoreMini : Application() {

    @Keep
    companion object {

        @JvmStatic
        lateinit var context: Context

        @JvmStatic
        var isDebug: Boolean = false

        /**
         * 获取进程名
         */
        @JvmStatic
        fun getProcessName(context: Context): String? {
            val pid = Process.myPid()
            val am = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
            val runningApps = am.runningAppProcesses ?: return null
            for (procInfo in runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName
                }
            }
            return null
        }
    }

    override fun onCreate() {
        super.onCreate()
//        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleObserver {
//
//        })
        registerActivityLifecycleCallbacks(DefaultActivityLifecycleCallback)
    }

    open fun initThirdLibs() {
        ZoomMediaLoader.getInstance().init(ZoomImageLoader())
    }

    override fun attachBaseContext(context: Context) {
        super.attachBaseContext(context)
        CoreMini.context = context
        MultiDex.install(context)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //内存低是清理管理的缓存
        Glide.get(this).clearMemory()
    }
}