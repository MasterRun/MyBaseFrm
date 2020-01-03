package com.jsongo.mobileim.rxplugin

import android.content.Context
import com.jsongo.core.util.CommonCallBack
import com.jsongo.core.util.RxBus
import com.jsongo.mobileim.MobileIM
import com.jsongo.mobileim.bean.Message
import com.jsongo.mobileim.core.MobileIMConfig
import com.jsongo.mobileim.operator.ChatMessageSender
import com.jsongo.mobileim.operator.SendCallback
import com.jsongo.mobileim.util.MobileIMMessageSign
import io.reactivex.android.schedulers.AndroidSchedulers
import net.openmob.mobileimsdk.android.core.LocalUDPDataSender

/**
 * @author ： jsongo
 * @date ： 2019/12/24 22:10
 * @desc :
 */
object MobileIMInvokeEvent {
    fun invoke(
        method: String,
        params: Map<String, Any?>?,
        callback: CommonCallBack?
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
            "isOnline" -> {
                if (MobileIM.isIMOnline) {
                    callback?.success(null)
                } else {
                    callback?.failed(-1, "", null)
                }
            }
            "send" -> {
                sendMessage(params, callback)
            }
            "logout" -> {
                logoutIM(params, callback)
            }
        }
    }

    /**
     * 初始化IM
     */
    fun initIM(
        params: Map<String, Any?>?,
        callback: CommonCallBack?,
        enableSuccessCallback: Boolean = true
    ) {
        val context = params?.get("context") as Context?
        if (context == null) {
            callback?.failed(-1, "context can't be null", null)
            return
        }
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
        callback: CommonCallBack?
    ) {
        val chatId = params?.get("chatid") as String
        val password = params.get("password") as String

        //登录回调
        fun registerLoginIMCallbackEvent() {
            RxBus.toFlowable().filter {
                it.code == MobileIMMessageSign.IM_LOGIN_EVENT
            }.observeOn(AndroidSchedulers.mainThread()).subscribe({
                when (it.data) {
                    //登录成功事件
                    MobileIMMessageSign.LOGIN_EVENT_SUCCESS -> {
                        callback?.success(null)
                    }
                    //登录失败事件
                    MobileIMMessageSign.LOGIN_EVENT_FAIL -> {
                        callback?.failed(-1, it.message, null)
                    }
                }
            }, {
                callback?.failed(-1, "", it)
            })
        }

        //注册登录回调
        registerLoginIMCallbackEvent()
        //发送登录消息，消息发送失败，直接失败回调，发送成功等待登录结果回调
        ChatMessageSender.loginIM(chatId, password, object : SendCallback {
            override fun onFailed() {
                super.onFailed()
                callback?.failed(-1, "", null)
            }
        })

    }

    /**
     * 退出登录
     */
    fun logoutIM(
        params: Map<String, Any?>?,
        callback: CommonCallBack?
    ) {
        LocalUDPDataSender.getInstance(MobileIM.context).sendLoginout()
        MobileIM.isIMOnline = false
        MobileIMConfig.init(MobileIM.context)
        callback?.success(null)
    }

    /**
     * 发送消息
     */
    fun sendMessage(params: Map<String, Any?>?, callback: CommonCallBack?) {
        val type = params?.get("type") as Int? ?: Message.TYPE_TEXT
        val from_id = params?.get("from_id") as String? ?: ""
        val to_id = params?.get("to_id") as String? ?: ""
        val content = params?.get("content") as String? ?: ""
        val conv_id = params?.get("conv_id") as String? ?: ""
        ChatMessageSender.sendMessageAsync(
            Message(
                sender_id = from_id,
                content = content,
                type = type,
                conv_id = conv_id
            ), to_id, object : SendCallback {
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