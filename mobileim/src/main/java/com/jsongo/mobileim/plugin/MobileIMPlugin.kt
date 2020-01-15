package com.jsongo.mobileim.plugin

import android.content.Context
import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.bean.ErrorPluginWrapper
import com.jsongo.core.plugin.IPlugin
import com.jsongo.core.plugin.MobileIM
import com.jsongo.core.util.RxBus
import com.jsongo.mobileim.bean.Message
import com.jsongo.mobileim.core.MobileIMConfig
import com.jsongo.mobileim.operator.ChatMessageSender
import com.jsongo.mobileim.operator.SendCallback
import com.jsongo.mobileim.util.MobileIMMessageSign
import io.reactivex.android.schedulers.AndroidSchedulers
import net.openmob.mobileimsdk.android.core.LocalUDPDataSender
import java.util.concurrent.*

/**
 * @author ： jsongo
 * @date ： 2020/1/15 20:47
 * @desc :
 */
class MobileIMPlugin : IPlugin {
    override val name = MobileIM

    override fun invoke(
        methodName: String,
        params: Map<String, Any?>?
    ): DataWrapper<MutableMap<String, Any?>> {

        return when (methodName) {
            "init" -> {
                initIM(params)
            }
            "login" -> {
                loginIM(params)
            }
            "isOnline" -> {
                DataWrapper(
                    hashMapOf(
                        Pair("result", com.jsongo.mobileim.MobileIM.isIMOnline as Any?)
                    )
                )
            }
            "send" -> {
                sendMessage(params)
            }
            "logout" -> {
                logoutIM(params)
            }
            else -> ErrorPluginWrapper.DEFAULT
        }
    }

    override fun route(
        pageName: String,
        params: Map<String, Any?>?
    ): DataWrapper<MutableMap<String, Any?>> {
        return ErrorPluginWrapper.DEFAULT
    }


    /**
     * 初始化IM
     */
    fun initIM(
        params: Map<String, Any?>?
    ): DataWrapper<MutableMap<String, Any?>> {
        val context = params?.get("context") as Context?
        if (context == null) {
            return ErrorPluginWrapper("context is null")
        }
        MobileIMConfig.init(context)
        return DataWrapper(hashMapOf(Pair("result", true as Any?)))
    }

    /**
     * 登录IM
     */
    fun loginIM(
        params: Map<String, Any?>?
    ): DataWrapper<MutableMap<String, Any?>> {
        return submit { countDownLatch ->
            val chatId = params?.get("chatid") as String
            val password = params.get("password") as String

            var result: DataWrapper<MutableMap<String, Any?>> = ErrorPluginWrapper.DEFAULT

            //登录回调
            fun registerLoginIMCallbackEvent() {
                RxBus.toFlowable().filter {
                    it.code == MobileIMMessageSign.IM_LOGIN_EVENT
                }.observeOn(AndroidSchedulers.mainThread()).subscribe({
                    when (it.data) {
                        //登录成功事件
                        MobileIMMessageSign.LOGIN_EVENT_SUCCESS -> {
                            result = DataWrapper(hashMapOf(Pair("result", true as Any?)))
                            countDownLatch.countDown()
                        }
                        //登录失败事件
                        MobileIMMessageSign.LOGIN_EVENT_FAIL -> {
                            result = ErrorPluginWrapper(it.message)
                            countDownLatch.countDown()
                        }
                    }
                }, {
                    result = ErrorPluginWrapper(it.message ?: "")
                    countDownLatch.countDown()
                })
            }

            //注册登录回调
            registerLoginIMCallbackEvent()
            //发送登录消息，消息发送失败，直接失败回调，发送成功等待登录结果回调
            ChatMessageSender.loginIM(chatId, password, object : SendCallback {
                override fun onFailed() {
                    super.onFailed()
                    result = ErrorPluginWrapper.DEFAULT
                    countDownLatch.countDown()

                }
            })
            countDownLatch.await()
            result
        }

    }

    /**
     * 退出登录
     */
    fun logoutIM(
        params: Map<String, Any?>?
    ): DataWrapper<MutableMap<String, Any?>> {
        LocalUDPDataSender.getInstance(com.jsongo.mobileim.MobileIM.context).sendLoginout()
        com.jsongo.mobileim.MobileIM.isIMOnline = false
        MobileIMConfig.init(com.jsongo.mobileim.MobileIM.context)
        return DataWrapper(hashMapOf(Pair("result", true as Any?)))
    }

    /**
     * 发送消息
     */
    fun sendMessage(params: Map<String, Any?>?): DataWrapper<MutableMap<String, Any?>> {
        return submit {
            var result: DataWrapper<MutableMap<String, Any?>> = ErrorPluginWrapper.DEFAULT

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
                        result = DataWrapper(hashMapOf(Pair("result", true as Any?)))
                        it.countDown()
                    }

                    override fun onFailed() {
                        super.onFailed()
                        result = ErrorPluginWrapper.DEFAULT
                        it.countDown()
                    }
                })
            it.await()
            result
        }
    }

    companion object {
        val threadPoolExecutor =
            ThreadPoolExecutor(4, Int.MAX_VALUE, 0, TimeUnit.MILLISECONDS, LinkedBlockingDeque())

        fun <T> submit(
            countDownCount: Int = 1,
            func: (CountDownLatch) -> T
        ): T {
            val future = threadPoolExecutor.submit(Callable<T> {
                val countDownLatch = CountDownLatch(countDownCount)
                func(countDownLatch)
            })
            return future.get()
        }

    }

}