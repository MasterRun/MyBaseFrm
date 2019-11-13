package com.jsongo.mobileim.operator;

import com.jsongo.core.util.BusEvent;
import com.jsongo.core.util.RxBus;
import com.jsongo.mobileim.util.Constants;

/**
 * @author jsongo
 * @date 2019/3/11 16:56
 */
public class ChatLoginCallback {
    public void onLoginSuccess() {
        BusEvent<Integer> busEvent = new BusEvent<>(Constants.IM_LOGIN_EVENT, "登录成功", Constants.LOGIN_EVENT_SUCCESS);
        RxBus.INSTANCE.post(busEvent);
    }

    public void onLoginFailed() {
        BusEvent<Integer> busEvent = new BusEvent<>(Constants.IM_LOGIN_EVENT, "登录失败", Constants.LOGIN_EVENT_FAIL);
        RxBus.INSTANCE.post(busEvent);
    }

    public void onLinkFailed(int dwErrorCode) {
        BusEvent<Integer> busEvent = new BusEvent<>(Constants.IM_LOGIN_EVENT, "您已掉线", Constants.LOGIN_EVENT_LINK_FAIL);
        RxBus.INSTANCE.post(busEvent);
    }
}
