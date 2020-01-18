package com.jsongo.mybasefrm.ui.main

import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.plugin.AppPlugin
import com.jsongo.core.plugin.MobileIM
import com.jsongo.core.util.CommonCallBack
import com.jsongo.core.util.RxBus
import com.jsongo.core.widget.RxToast
import com.safframework.log.L
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author ： jsongo
 * @date ： 2019/11/7 12:57
 * @desc : ViewModel 测试
 */
class MainViewModel : BaseViewModel() {

    /**
     * 首页底部tab的角标数量
     */
    val mainTabTips = MutableLiveData<IntArray>()

    /**
     * im 状态标识
     * -2：掉线
     * -1：登录失败
     * 0: 初始值
     * 1：登录成功
     * 2：已登录
     */
    val imStatusCode = MutableLiveData(0)

    /**
     * IM状态
     */
    var imStateMsg = ""

    /**
     * finish当前页面
     */
    val finish = MutableLiveData<Boolean>(false)

    init {
        //初始化角标数量
        mainTabTips.value = intArrayOf(0, 0, 0, 0)
    }

    /**
     * 设置角标数量
     */
    fun setMainTabTipCount(index: Int, count: Int) {
        val value = mainTabTips.value
        value?.set(index, count)
        mainTabTips.value = value
    }

    /**
     * 检查IM，是否启用，是否登录
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun checkIM() {
        //如果启用IM
        if (AppPlugin.isEnabled(MobileIM)) {
            //检查是否登录
            val result = AppPlugin.invoke(MobileIM, "isOnline", null)
            if (result.code < 0 || result.data?.get("result") != true) {
                //未登录，进行登录
                loginIM()
            } else {
                imStatusCode.value = 2
                imStateMsg = "已登录"
                //已登录
                //注册IM接收器
                regIMReceiver()
                //发送消息测试
                sendIMMsgTest()
            }
        }
    }

    /**
     * 登录IM
     */
    private fun loginIM() {
        AppPlugin.invoke(MobileIM, "_login", hashMapOf(
            Pair("chatid", CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID)),
            Pair("password", CommonDbOpenHelper.getValue(CommonDbKeys.USER_PASSWORD))
        ), object : CommonCallBack {
            override fun success(data: Map<String, Any?>?) {
                imStatusCode.value = 1
                imStateMsg = "IM 登录成功"
                //已登录
                //注册IM接收器
                regIMReceiver()
                //发送消息测试
                sendIMMsgTest()
            }

            override fun failed(code: Int, msg: String, throwable: Throwable?) {
                throwable?.printStackTrace()
                val errorMsg = if (!TextUtils.isEmpty(msg)) {
                    msg
                } else if (!TextUtils.isEmpty(throwable?.message)) {
                    throwable?.message!!
                } else {
                    "登录IM失败！"
                }
                imStatusCode.value = -1
                imStateMsg = errorMsg
                L.e(errorMsg)
            }
        })
    }

    fun sendIMMsgTest() {
        //如果启用mobileim，开启发送消息测试
        if (AppPlugin.isEnabled(MobileIM)) {
            val chatId = CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID)
            if (TextUtils.isEmpty(chatId)) {
                RxToast.error("chatId is null or empty")
                return
            }
            //发消息测试
            Observable.intervalRange(0, 1, 5, 10, TimeUnit.SECONDS, Schedulers.io())
                .map {
                    AppPlugin.invoke(MobileIM, "_send", hashMapOf(
                        Pair("type", 1),
                        Pair("from_id", chatId),
                        Pair("to_id", "0"),
                        Pair("content", "messagesend message test ")
                    ), object : CommonCallBack {
                        override fun success(data: Map<String, Any?>?) {
                            L.e("send message success")
                        }

                        override fun failed(code: Int, msg: String, throwable: Throwable?) {
                            L.e("send message failed")
                        }
                    })
                }.subscribe()
        }
    }

    /**
     * 注册MobileIM消息接收
     */
    fun regIMReceiver() {
        //如果启用mobileim，开启mobileim监听
        if (AppPlugin.isEnabled(MobileIM)) {
            val disposable = RxBus.toFlowable()
                .filter {
                    it.code == 15001 || it.code == 15002 || it.code == 15003
                    //MobileIMMessageSign.isMobileIMMessage(it.code)
                }.map {
                    //if (it.code == MobileIMMessageSign.IM_LOGIN_EVENT && it.data == MobileIMMessageSign.LOGIN_EVENT_LINK_FAIL) {
                    if (it.code == 15001 && it.data == 3) {
                        //掉线会重连，目前不处理
//                        imStatusCode.value = -2
//                        imStateMsg = it.message
                        L.e(it.message)
                    } else {
                        L.e("mobileim收到消息：${it}")
                    }
                }.subscribe()
            compositeDisposable.add(disposable)
        }
    }

    /**
     * 退出登录
     */
    fun logout() {

        fun clearDbAndFinish() {
            //清除数据
            CommonDbOpenHelper.deleteKey(CommonDbKeys.USER_GUID)
            CommonDbOpenHelper.deleteKey(CommonDbKeys.USER_PASSWORD)
            CommonDbOpenHelper.deleteKey(CommonDbKeys.USER_INFO)
            //关闭页面
            finish.value = true
        }

        //如果启用mobileim，退出登录IM
        if (AppPlugin.isEnabled(MobileIM)) {
            AppPlugin.invoke(MobileIM, "logout", null)
        }
        clearDbAndFinish()
    }

}
