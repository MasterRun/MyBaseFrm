package com.jsongo.core.plugin_manager

import com.jsongo.core.util.CommonCallBack

/**
 * 组件事件
 */
abstract class PluginEvent(
    val params: Map<String, Any?>?,
    val callback: CommonCallBack?
) {

    /**
     * 组件方法调用事件对象
     */
    class Invoke(invokePath: String, params: Map<String, Any?>?, callback: CommonCallBack?) :
        PluginEvent(params, callback) {
        constructor(
            pluginName: String,
            method: String,
            params: Map<String, Any?>?,
            callback: CommonCallBack?
        ) : this("$pluginName.$method", params, callback)

        val pluginName: String
        val method: String

        init {
            val split = invokePath.split(".")
            pluginName = split[0]
            method = split[1]
        }
    }

    /**
     * 组件页面打开事件对象
     */
    class Route(invokePath: String, params: Map<String, Any?>?, callback: CommonCallBack?) :
        PluginEvent(params, callback) {
        constructor(
            pluginName: String,
            pageName: String,
            params: Map<String, Any?>?,
            callback: CommonCallBack?
        ) : this("/$pluginName/$pageName", params, callback)

        val pluginName: String
        val pageName: String

        init {
            val split = invokePath.split("/")
            pluginName = split[1]
            pageName = split[2]
        }
    }
}