package com.jsongo.mobileim.operator

import com.jsongo.core.util.BusEvent
import com.jsongo.core.util.LogcatUtil
import com.jsongo.core.util.RxBus
import com.jsongo.mobileim.util.MobileIMMessageSign

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
        LogcatUtil.d("IM 登录成功")
        RxBus.post(busEvent)
    }

    fun onLoginFailed() {
        val busEvent = BusEvent(
            MobileIMMessageSign.IM_LOGIN_EVENT,
            "登录失败",
            MobileIMMessageSign.LOGIN_EVENT_FAIL
        )
        LogcatUtil.d("IM 登录失败")
        RxBus.post(busEvent)
    }

    fun onLinkFailed(dwErrorCode: Int) {
        val busEvent = BusEvent(
            MobileIMMessageSign.IM_LOGIN_EVENT,
            "您已掉线",
            MobileIMMessageSign.LOGIN_EVENT_LINK_FAIL
        )
        LogcatUtil.d("IM 掉线")
        RxBus.post(busEvent)
    }
}
