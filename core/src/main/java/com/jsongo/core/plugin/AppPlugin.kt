package com.jsongo.core.plugin

import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.util.CommonCallBack
import java.util.*

/**
 * @author ： jsongo
 * @date ： 2020/1/15 21:52
 * @desc :
 */

const val MobileIM = "mobileim"

object AppPlugin {

    /**
     * 已注册的组件结合
     */
    val plugins = HashMap<String, IPlugin>()

    /**
     * 组件是否启用
     */
    fun isEnabled(pluginName: String): Boolean {
        return plugins.keys.contains(pluginName)
    }

    /**
     * 注册组件
     */
    fun register(plugin: IPlugin) {
        plugins[plugin.name] = plugin
    }

    /**
     * 方法调用
     */
    fun invoke(
        pluginName: String,
        methodName: String,
        params: Map<String, Any?>?,
        callBack: CommonCallBack? = null
    ): DataWrapper<MutableMap<String, Any?>> {
        val iPlugin = plugins[pluginName]
        if (iPlugin == null) {
            throw Exception("plugin not found : $pluginName")
        } else {
            return iPlugin.invoke(methodName, params, callBack)
        }
    }

    /**
     * 路由打开页面
     */
    fun route(
        pluginName: String,
        pageName: String,
        params: Map<String, Any?>?,
        callBack: CommonCallBack? = null
    ): DataWrapper<MutableMap<String, Any?>> {
        val iPlugin = plugins[pluginName]
        if (iPlugin == null) {
            throw Exception("plugin not found : $pluginName")
        } else {
            return iPlugin.route(pageName, params, callBack)
        }
    }
}