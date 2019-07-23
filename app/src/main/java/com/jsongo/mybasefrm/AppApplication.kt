package com.jsongo.mybasefrm

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import com.bumptech.glide.Glide
import com.jsongo.ajs.AJs
import com.jsongo.core.BaseCore
import org.jetbrains.annotations.Contract

/**
 * @author jsongo
 * @date 2018/9/3 18:40
 */
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseCore.init()
        AJs.init(this)
        BaseCore.isDebug = isDebug
    }

    override fun attachBaseContext(context: Context) {
        AppApplication.context = context;
        super.attachBaseContext(context)
        BaseCore.attachBaseContext(context)
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

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        val isDebug: Boolean
            @Contract(pure = true)
            get() = false
    }
}
