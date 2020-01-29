package com.jsongo.im

import android.content.Context
import com.jsongo.core.plugin.AppPlugin
import com.jsongo.im.mobileim.core.MobileIMConfig
import com.jsongo.im.plugin.MobileIMPlugin

/**
 * @author ： jsongo
 * @date ： 2019/11/12 18:04
 * @desc :
 */
object MobileIM {
    lateinit var context: Context

    var isIMOnline = false

    fun init(context: Context) {
        this.context = context

        //初始化
        MobileIMConfig.init(context)

        //注册plugin
        AppPlugin.register(MobileIMPlugin())
    }

}