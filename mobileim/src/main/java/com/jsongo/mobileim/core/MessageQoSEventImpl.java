package com.jsongo.mobileim.core;

import com.safframework.log.L;

import net.openmob.mobileimsdk.android.event.MessageQoSEvent;
import net.openmob.mobileimsdk.server.protocal.Protocal;

import java.util.ArrayList;

/**
 * @author jsongo
 * @date 2019/3/2 10:52
 * <p>
 * QoS相关事件回调实现类
 */
public class MessageQoSEventImpl implements MessageQoSEvent {

    // 对方已成功收到消息的通知
    @Override
    public void messagesBeReceived(String theFingerPrint) {
        L.e(MessageQoSEventImpl.class.getName(), "收到对方已收到消息事件的通知，消息指纹码=" + theFingerPrint);
    }

    // 消息无法完成实时送达的通知
    @Override
    public void messagesLost(ArrayList<Protocal> lostMessages) {
        L.e(MessageQoSEventImpl.class.getName(), "收到系统的未实时送达事件通知，当前共有"
                + lostMessages.size() + "个包QoS保证机制结束，判定为【无法实时送达】！");
    }
}
