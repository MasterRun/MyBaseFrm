package com.jsongo.mobileim

import android.content.Context
import com.jsongo.mobileim.core.MobileIMConfig
import com.jsongo.mobileim.rxplugin.MobileIMPlugin

/**
 * @author ： jsongo
 * @date ： 2019/11/12 18:04
 * @desc :
 */
object MobileIM {
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context

        MobileIMConfig.init(context)

        MobileIMPlugin.register()
    }

}