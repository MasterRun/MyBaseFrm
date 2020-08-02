package com.jsongo.core_mini

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
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

    companion object {

        @JvmStatic
        lateinit var context: Context

        @JvmStatic
        var isDebug: Boolean = false
    }

    override fun onCreate() {
        super.onCreate()

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