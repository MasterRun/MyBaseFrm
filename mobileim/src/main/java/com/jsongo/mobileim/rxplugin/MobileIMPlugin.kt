package com.jsongo.mobileim.rxplugin

import android.annotation.SuppressLint
import com.jsongo.core.plugin_manager.InvokeSubscriber
import com.jsongo.core.plugin_manager.PluginDispatcher
import com.jsongo.core.plugin_manager.Plugins
import com.jsongo.core.plugin_manager.RouteSubscriber
import io.reactivex.functions.Consumer

/**
 * @author ： jsongo
 * @date ： 2019/12/22 15:18
 * @desc :
 */
object MobileIMPlugin {

    @SuppressLint("CheckResult")
    @JvmStatic
    fun register() {
        //是组件方法调用
        PluginDispatcher.ofInvoke(Plugins.MobileIM)
            .subscribe(InvokeSubscriber(Consumer {
                MobileIMInvokeEvent.invoke(it.method, it.params, it.callback)
            }))
        //是组件打开页面
        PluginDispatcher.ofRoute(Plugins.MobileIM)
            .subscribe(RouteSubscriber(Consumer {
                MobileIMRouteEvent.route(it.pageName, it.params, it.callback)
            }))
    }

}
