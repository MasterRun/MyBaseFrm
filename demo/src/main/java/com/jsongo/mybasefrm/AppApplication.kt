package com.jsongo.mybasefrm

import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import com.bumptech.glide.Glide
import com.jsongo.ajs.AJs
import com.jsongo.core.BaseCore
import com.jsongo.mobileim.MobileIM
import com.jsongo.ui.BaseUI
import com.jsongo.ui.enhance.UIEnhance
import org.jetbrains.annotations.Contract

/**
 * @author jsongo
 * @date 2018/9/3 18:40
 */
class AppApplication : Application() {

    companion object {

        lateinit var context: Context

        val isDebug: Boolean
            @Contract(pure = true)
            get() = true && BuildConfig.DEBUG
    }

    override fun onCreate() {
        super.onCreate()
        //普通module初始化
        BaseCore.init(isDebug)
        AJs.init(this)
        BaseUI.init(this)
        UIEnhance.init(this)
        //以下是组件初始化和注册，单写方法
        initMobileIM()
    }

    fun initMobileIM() {
        MobileIM.init(this)
    }

    override fun attachBaseContext(context: Context) {
        AppApplication.context = context
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
}
