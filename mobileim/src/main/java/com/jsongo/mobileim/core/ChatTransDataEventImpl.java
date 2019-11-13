package com.jsongo.mobileim.core;

import com.jsongo.mobileim.operator.ChatMessageReceiver;

import net.openmob.mobileimsdk.android.event.ChatTransDataEvent;

/**
 * @author jsongo
 * @date 2019/3/2 10:50
 * <p>
 * 实时消息事件回调实现类：
 */
public class ChatTransDataEventImpl implements ChatTransDataEvent {
    private ChatMessageReceiver chatMessageReceiver;

    ChatTransDataEventImpl(ChatMessageReceiver chatMessageReceiver) {
        this.chatMessageReceiver = chatMessageReceiver;
    }

    // 收到即时通讯消息通知
    @Override
    public void onTransBuffer(String fingerPrintOfProtocal, String dwUserid, String dataContent, int i) {
        chatMessageReceiver.onReceive(dwUserid, dataContent);
    }

    // 收到服务端反馈的错误信息通知
    @Override
    public void onErrorResponse(int errorCode, String errorMsg) {
        chatMessageReceiver.onError(errorCode, errorMsg);
    }
}
