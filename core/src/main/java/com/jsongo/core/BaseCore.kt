package com.jsongo.core

import android.content.Context
import com.jsongo.core.crash.CrashHandler
import com.jsongo.core_mini.CoreMini
import com.safframework.log.L

/**
 * author ： jsongo
 * createtime ： 2019/7/23 8:58
 * desc :
 */
open class BaseCore : CoreMini() {

    companion object {

        @JvmStatic
        lateinit var context: Context

        @JvmStatic
        var isDebug: Boolean = CoreMini.isDebug
    }

    override fun onCreate() {
        super.onCreate()

        CrashHandler.init()

        //如果不是debug,移除所有打印
        if (!isDebug) {
            L.printers().clear()
        }
    }

    override fun attachBaseContext(context: Context) {
        BaseCore.context = context
        super.attachBaseContext(context)
    }
}
