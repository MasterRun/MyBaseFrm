package com.jsongo.core.plugin_manager

import java.util.ArrayList

/**
 * @author ： jsongo
 * @date ： 2019/12/24 16:55
 * @desc :
 */

object Plugins {

    val MobileIM = "mobileim"

    /**
     * 启用的组件
     */
    val enabledPlugins = ArrayList<String>()

    /**
     * 某组件是否启用
     */
    fun isPluginEnabled(pluginName: String): Boolean {
        return enabledPlugins.contains(pluginName)
    }

    /**
     * 标记组件启用
     */
    fun markPluginEnabled(pluginName: String) {
        enabledPlugins.add(pluginName)
    }


}