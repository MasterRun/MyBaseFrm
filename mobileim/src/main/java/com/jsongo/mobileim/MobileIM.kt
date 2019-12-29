package com.jsongo.mobileim

import android.content.Context
import com.jsongo.core.plugin_manager.Plugins
import com.jsongo.mobileim.core.MobileIMConfig
import com.jsongo.mobileim.rxplugin.MobileIMPlugin

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

        //标记组件启用
        Plugins.markPluginEnabled(Plugins.MobileIM)

        //初始化
        MobileIMConfig.init(context)

        //注册组件事件监听
        MobileIMPlugin.register()
    }

}