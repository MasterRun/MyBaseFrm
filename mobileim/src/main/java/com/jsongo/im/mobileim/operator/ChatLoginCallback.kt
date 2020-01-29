package com.jsongo.im.mobileim.operator

import com.jsongo.core.util.BusEvent
import com.jsongo.core.util.LogcatUtil
import com.jsongo.core.util.RxBus
import com.jsongo.im.MobileIM
import com.jsongo.im.util.MobileIMMessageSign

/**
 * @author jsongo
 * @date 2019/3/11 16:56
 */
class ChatLoginCallback {
    fun onLoginSuccess() {
        val busEvent = BusEvent(
            MobileIMMessageSign.IM_LOGIN_EVENT,
            "登录成功",
            MobileIMMessageSign.LOGIN_EVENT_SUCCESS
        )
        MobileIM.isIMOnline = true
        LogcatUtil.d("IM 登录成功")
        RxBus.post(busEvent)
    }

    fun onLoginFailed() {
        val busEvent = BusEvent(
            MobileIMMessageSign.IM_LOGIN_EVENT,
            "登录失败",
            MobileIMMessageSign.LOGIN_EVENT_FAIL
        )
        MobileIM.isIMOnline = false
        LogcatUtil.d("IM 登录失败")
        RxBus.post(busEvent)
    }

    fun onLinkFailed(dwErrorCode: Int) {
        val busEvent = BusEvent(
            MobileIMMessageSign.IM_LOGIN_EVENT,
            "您已掉线",
            MobileIMMessageSign.LOGIN_EVENT_LINK_FAIL
        )
        MobileIM.isIMOnline = false
        LogcatUtil.d("IM 掉线")
        RxBus.post(busEvent)
    }
}
