package com.jsongo.mobileim.plugin

import android.content.Context
import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.bean.ErrorPluginWrapper
import com.jsongo.core.util.CommonCallBack
import com.jsongo.core.util.RxBus
import com.jsongo.mobileim.MobileIM
import com.jsongo.mobileim.bean.Message
import com.jsongo.mobileim.core.MobileIMConfig
import com.jsongo.mobileim.operator.ChatMessageSender
import com.jsongo.mobileim.util.MobileIMMessageSign
import io.reactivex.android.schedulers.AndroidSchedulers
import net.openmob.mobileimsdk.android.core.LocalUDPDataSender

/**
 * @author ： jsongo
 * @date ： 2020/1/16 18:14
 * @desc :
 */
object MobileIMInvoke {

    /**
     * 初始化IM
     */
    fun initIM(
        params: Map<String, Any?>?
    ): DataWrapper<MutableMap<String, Any?>> {
        val context =
            params?.get("context") as Context? ?: return ErrorPluginWrapper("context is null")
        MobileIMConfig.init(context)
        return DataWrapper(hashMapOf(Pair("result", true as Any?)))
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
        ChatMessageSender.loginIM(chatId, password, callback)
    }

    /**
     * 是否在线
     */
    fun isOnline(): DataWrapper<MutableMap<String, Any?>> {
        return DataWrapper(
            hashMapOf(
                Pair("result", MobileIM.isIMOnline as Any?)
            )
        )
    }

    /**
     * 退出登录
     */
    fun logoutIM(): DataWrapper<MutableMap<String, Any?>> {
        val sendLoginout = LocalUDPDataSender.getInstance(MobileIM.context).sendLoginout()
        MobileIM.isIMOnline = false
        MobileIMConfig.init(MobileIM.context)
        return DataWrapper(
            hashMapOf(
                Pair("result", true as Any?),
                Pair("sengLogout", sendLoginout)
            )
        )
    }

    /**
     * 发送消息
     */
    fun sendMessage(
        params: Map<String, Any?>?,
        callback: CommonCallBack?
    ) {
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
            ), to_id, callback
        )
    }
}