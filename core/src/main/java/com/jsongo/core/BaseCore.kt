package com.jsongo.core

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import androidx.multidex.MultiDex
import com.bumptech.glide.Glide
import com.jsongo.core.common.DefaultActivityLifecycleCallback
import com.jsongo.core.crash.CrashHandler
import com.safframework.log.L

/**
 * author ： jsongo
 * createtime ： 2019/7/23 8:58
 * desc :
 */
open class BaseCore : Application() {

    companion object {

        @JvmStatic
        lateinit var context: Context

        @JvmStatic
        var isDebug: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(DefaultActivityLifecycleCallback)

        CrashHandler.init()

        //如果不是debug,移除所有打印
        if (!isDebug) {
            L.printers().clear()
        }
    }

    override fun attachBaseContext(context: Context) {
        BaseCore.context = context
        super.attachBaseContext(context)
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
