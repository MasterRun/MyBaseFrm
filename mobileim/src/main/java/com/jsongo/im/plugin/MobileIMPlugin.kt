package com.jsongo.im.plugin

import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.bean.ErrorPluginWrapper
import com.jsongo.core.common.MapCallBack
import com.jsongo.core.plugin.IPlugin
import com.jsongo.core.plugin.MobileIM

/**
 * @author ： jsongo
 * @date ： 2020/1/15 20:47
 * @desc :
 */
class MobileIMPlugin : IPlugin {
    override val name = MobileIM

    override fun invoke(
        methodName: String,
        params: Map<String, Any?>?,
        callback: MapCallBack?
    ): DataWrapper<MutableMap<String, Any?>> {

        return when (methodName) {
            "init" -> {
                MobileIMInvoke.initIM(params)
            }
            "_login" -> {
                MobileIMInvoke.loginIM(params, callback)
                DataWrapper.PLUGIN_WRAP_NO_RESULT
            }
            "isOnline" -> {
                MobileIMInvoke.isOnline()
            }
            "_send" -> {
                MobileIMInvoke.sendMessage(params, callback)
                DataWrapper.PLUGIN_WRAP_NO_RESULT
            }
            "logout" -> {
                MobileIMInvoke.logoutIM()
            }
            "_getConvs" -> {
                MobileIMInvoke.getConvs(params, callback)
                DataWrapper.PLUGIN_WRAP_NO_RESULT
            }
            else -> ErrorPluginWrapper.DEFAULT
        }
    }

    override fun route(
        pageName: String,
        params: Map<String, Any?>?,
        callback: MapCallBack?
    ): DataWrapper<MutableMap<String, Any?>> {
        return when (pageName) {
            "chat" -> {
                MobileIMRoute.goChat(params)
            }
            else -> ErrorPluginWrapper.DEFAULT
        }
    }

}