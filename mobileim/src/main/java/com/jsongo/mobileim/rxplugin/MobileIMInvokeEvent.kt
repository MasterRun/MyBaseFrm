package com.jsongo.mobileim.rxplugin

import android.content.Context
import com.jsongo.core.rxplugin.PluginEvent
import com.jsongo.mobileim.bean.Message
import com.jsongo.mobileim.core.MobileIMConfig
import com.jsongo.mobileim.operator.ChatMessageSender
import com.jsongo.mobileim.operator.SendCallback

/**
 * @author ： jsongo
 * @date ： 2019/12/24 22:10
 * @desc :
 */
object MobileIMInvokeEvent {
    fun invoke(
        method: String,
        params: Map<String, Any?>?,
        callback: PluginEvent.EventCallback?
    ) {
        when (method) {
            "init" -> {
                initIM(params, callback)
            }
            "login" -> {
                loginIM(params, callback)
            }
            "init&login" -> {
                initIM(params, callback, false)
                loginIM(params, callback)
            }
            "send" -> {
                sendMessage(params, callback)
            }
        }
    }

    /**
     * 初始化IM
     */
    fun initIM(
        params: Map<String, Any?>?,
        callback: PluginEvent.EventCallback?,
        enableSuccessCallback: Boolean = true
    ) {
        val context = params?.get("context") as Context
        MobileIMConfig.init(context)
        if (enableSuccessCallback) {
            callback?.success(null)
        }
    }

    /**
     * 登录IM
     */
    fun loginIM(
        params: Map<String, Any?>?,
        callback: PluginEvent.EventCallback?
    ) {
        val charId = params?.get("chatid") as String
        val password = params.get("password") as String

        MobileIMConfig.loginIM(charId, password, object : SendCallback {
            override fun onSuccess() {
                super.onSuccess()
                callback?.success(null)
            }

            override fun onFailed() {
                super.onFailed()
                callback?.failed(-1, "", null)
            }
        })

    }


    /**
     * 发送消息
     */
    fun sendMessage(params: Map<String, Any?>?, callback: PluginEvent.EventCallback?) {
        val type = params?.get("type") as Int? ?: Message.TYPE_TEXT
        val content = params?.get("content") as String? ?: ""
        val conv_id = params?.get("conv_id") as String? ?: ""
        ChatMessageSender.sendMessageAsync(
            Message(
                sender_id = "testChatId",
                content = content,
                type = type,
                conv_id = conv_id
            ), "0", object : SendCallback {
                override fun onSuccess() {
                    super.onSuccess()
                    callback?.success(null)
                }

                override fun onFailed() {
                    super.onFailed()
                    callback?.failed(-1, "", null)
                }
            })
    }

}