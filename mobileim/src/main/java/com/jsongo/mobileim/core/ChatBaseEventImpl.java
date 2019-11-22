package com.jsongo.mobileim.core;

import com.jsongo.mobileim.operator.ChatLoginCallback;
import com.safframework.log.L;

import net.openmob.mobileimsdk.android.event.ChatBaseEvent;

/**
 * @author jsongo
 * @date 2019/3/2 10:46
 * <p>
 * 框架基本事件回调实现类
 */
public class ChatBaseEventImpl implements ChatBaseEvent {
    // 登陆/掉线重连结果通知
    private final ChatLoginCallback chatLoginCallback;

    ChatBaseEventImpl(ChatLoginCallback chatLoginCallback) {
        this.chatLoginCallback = chatLoginCallback;
    }

    @Override
    public void onLoginMessage(int dwErrorCode) {
        if (dwErrorCode == 0) {
            L.e(ChatBaseEventImpl.class.getName(), "登录成功，code = " + dwErrorCode);
            chatLoginCallback.onLoginSuccess();
        } else {
            L.e(ChatBaseEventImpl.class.getName(), "登录失败，code = " + dwErrorCode);
            chatLoginCallback.onLoginFailed();
        }
    }

    // 掉线事件通知
    @Override
    public void onLinkCloseMessage(int dwErrorCode) {
        L.e(ChatBaseEventImpl.class.getName(), "网络连接出错关闭了，error：" + dwErrorCode);
        chatLoginCallback.onLinkFailed(dwErrorCode);
    }
}