package com.jsongo.core

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDex
import com.jsongo.core.crash.CrashHandler
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
    }

    fun attachBaseContext(context: Context) {
        this.context = context
        MultiDex.install(context)
    }

    var isDebug: Boolean = false
}
