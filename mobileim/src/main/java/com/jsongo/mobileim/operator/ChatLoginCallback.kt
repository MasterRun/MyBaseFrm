package com.jsongo.mobileim.operator

import com.jsongo.core.util.BusEvent
import com.jsongo.core.util.RxBus
import com.jsongo.mobileim.util.Constants
import com.safframework.log.L

/**
 * @author jsongo
 * @date 2019/3/11 16:56
 */
class ChatLoginCallback {
    fun onLoginSuccess() {
        val busEvent = BusEvent(Constants.IM_LOGIN_EVENT, "登录成功", Constants.LOGIN_EVENT_SUCCESS)
        L.d("IM 登录成功")
        RxBus.post(busEvent)
    }

    fun onLoginFailed() {
        val busEvent = BusEvent(Constants.IM_LOGIN_EVENT, "登录失败", Constants.LOGIN_EVENT_FAIL)
        L.d("IM 登录失败")
        RxBus.post(busEvent)
    }

    fun onLinkFailed(dwErrorCode: Int) {
        val busEvent = BusEvent(Constants.IM_LOGIN_EVENT, "您已掉线", Constants.LOGIN_EVENT_LINK_FAIL)
        L.d("IM 掉线")
        RxBus.post(busEvent)
    }
}
