package com.jsongo.core

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDex
import com.jsongo.core.crash.CrashHandler
import com.safframework.log.L
import com.vondear.rxtool.RxTool

/**
 * author ： jsongo
 * createtime ： 2019/7/23 8:58
 * desc :
 */
@SuppressLint("StaticFieldLeak")
object BaseCore {

    lateinit var context: Context

    fun init() {
        CrashHandler.init()
        //init RxTool without init RxCrashTool
        val contextField = RxTool::class.java.getDeclaredField("context")
        contextField.isAccessible = true
        contextField.set(null, context.applicationContext)

        //如果不是debug,移除所有打印
        if (!isDebug) {
            L.printers().clear()
        }
    }

    fun attachBaseContext(context: Context) {
        this.context = context
        MultiDex.install(context)
    }

    var isDebug: Boolean = true && BuildConfig.DEBUG
}
