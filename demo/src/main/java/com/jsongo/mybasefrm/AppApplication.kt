package com.jsongo.mybasefrm

import android.content.Context
import com.jsongo.ajs.AJs
import com.jsongo.core.BaseCore
import com.jsongo.ui.BaseUI
import com.jsongo.ui.enhance.UIEnhance
import org.jetbrains.annotations.Contract

/**
 * @author jsongo
 * @date 2018/9/3 18:40
 */
class AppApplication : BaseCore() {

    companion object {

        lateinit var context: Context

        val isDebug: Boolean
            @Contract(pure = true)
            get() = true && BuildConfig.DEBUG
    }

    override fun onCreate() {
        super.onCreate()
        //普通module初始化

        context = BaseCore.context
        BaseCore.isDebug = isDebug
        initThirdLibs()
        AJs.init(this)
        BaseUI.init(this)
        UIEnhance.init(this)
        //以下是组件初始化和注册，单写方法
        initMobileIM()
    }

    fun initMobileIM() {
//        MobileIM.init(this)
    }
}
