package com.jsongo.im.plugin

import android.content.Context
import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.bean.ErrorPluginWrapper
import com.jsongo.core.network.NetFailedException
import com.jsongo.core.common.CommonCallBack
import com.jsongo.core.common.RxBus
import com.jsongo.im.MobileIM
import com.jsongo.im.bean.Message
import com.jsongo.im.data.repository.MobileHttpRequestManager
import com.jsongo.im.mobileim.core.MobileIMConfig
import com.jsongo.im.mobileim.operator.ChatMessageSender
import com.jsongo.im.util.MobileIMMessageSign
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
        ChatMessageSender.loginIM(chatId, password, object :
            CommonCallBack {
            override fun success(data: Map<String, Any?>?) {
            }

            override fun failed(code: Int, msg: String, throwable: Throwable?) {
                callback?.failed(code, msg, throwable)
            }
        })
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

    /**
     * 获取会话列表
     */
    fun getConvs(
        params: Map<String, Any?>?,
        callback: CommonCallBack?
    ) {
        val scope = params?.get("scope") as CoroutineScope?
        if (scope == null) {
            callback?.failed(-1, "scope不能为空！", null)
            return
        }
        scope.launch {
            try {
                val resultData = withContext(Dispatchers.IO) {
                    val conversations = MobileHttpRequestManager.getConversations()
                    val resultData = ArrayList<Map<String, Any?>>(2)
                    for (conversation in conversations) {
                        resultData.add(
                            hashMapOf(
                                Pair("username", conversation.convName),
                                Pair("lastMessage", conversation.lastMessage?.content ?: ""),
                                Pair("time", conversation.lastMessage?.send_time),
                                Pair("avatar", conversation.avatar),
                                Pair("unreadCount", conversation.unreadCount.toString())
                            )
                        )
                    }
                    resultData
                }
                callback?.success(hashMapOf(Pair("convs", resultData)))
            } catch (e: NetFailedException) {
                e.printStackTrace()
                callback?.failed(-1, e.message ?: "", e)
            }
        }
    }
}