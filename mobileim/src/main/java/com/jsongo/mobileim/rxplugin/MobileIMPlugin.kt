package com.jsongo.mobileim.rxplugin

import android.annotation.SuppressLint
import com.jsongo.core.plugin_manager.PluginEvent
import com.jsongo.core.plugin_manager.Plugins
import com.jsongo.core.plugin_manager.PluginDispatcher
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

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
            .subscribe(object : Subscriber<PluginEvent.Invoke> {
                var pluginInvoke: PluginEvent.Invoke? = null

                override fun onSubscribe(s: Subscription) {
                    s.request(Long.MAX_VALUE)
                }

                override fun onNext(t: PluginEvent.Invoke) {
                    pluginInvoke = t
                    MobileIMInvokeEvent.invoke(t.method, t.params, t.callback)
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                    pluginInvoke?.callback?.failed(-1, t.message ?: "", t)
                }

                override fun onComplete() {
                }
            })
        //是组件打开页面
        PluginDispatcher.ofRoute(Plugins.MobileIM)
            .subscribe(object : Subscriber<PluginEvent.Route> {
                var pluginRoute: PluginEvent.Route? = null

                override fun onSubscribe(s: Subscription) {
                    s.request(Long.MAX_VALUE)
                }

                override fun onNext(t: PluginEvent.Route) {
                    pluginRoute = t
                    MobileIMRouteEvent.route(t.pageName, t.params, t.callback)
                }

                override fun onError(t: Throwable) {
                    t.printStackTrace()
                    pluginRoute?.callback?.failed(-1, t.message ?: "", t)
                }

                override fun onComplete() {
                }
            })
    }

}
