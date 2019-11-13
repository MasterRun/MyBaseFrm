package com.jsongo.mobileim.operator;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.jsongo.core.network.ApiManager;
import com.jsongo.core.util.StringCompress;
import com.jsongo.mobileim.MobileIM;
import com.jsongo.mobileim.bean.Message;
import com.jsongo.mobileim.bean.UdpData;
import com.jsongo.mobileim.bean.UdpDataType;

import net.openmob.mobileimsdk.android.core.LocalUDPDataSender;

/**
 * @author jsongo
 * @date 2019/3/10 21:54
 */
public class ChatMessageSender {

    private static Gson gson = ApiManager.INSTANCE.getGson();

    public static void sendMessageAsync(Message message, String toUserId, SendCallback callback) {
        if (TextUtils.isEmpty(toUserId) || message == null) {
            return;
        }
        UdpData<Message> messageUdpData = new UdpData<>(message.getSender_id(), toUserId,
                UdpDataType.Message.ordinal(), message);
        String msg = gson.toJson(messageUdpData, UdpData.Companion.getMessageUdpDataType());
        String gzip = StringCompress.gzip(msg);
        new SenderAsync(MobileIM.context, gzip, toUserId, callback).execute();
    }
/*
    public static void sendToServerAsync(Conversation conversation, SendCallback callback) {
        String toUserId = "0";
        UdpData<Conversation> conversationUdpData = new UdpData<>(MobileIM.user.getChat_id(),
                toUserId, UdpDataType.Conversation.ordinal(), conversation);
        String conv = gson.toJson(conversationUdpData, UdpData.Companion.getConversationUdpData());
        new SenderAsync(MobileIM.context, conv, toUserId, callback).execute();
    }*/

    static class SenderAsync extends LocalUDPDataSender.SendCommonDataAsync {
        private final SendCallback callback;

        SenderAsync(Context context, String dataContentWidthStr, String toUserId, SendCallback callback) {
            super(context, dataContentWidthStr, toUserId);
            this.callback = callback;
        }

        @Override
        protected void onPostExecute(Integer code) {
            if (code == 0) {
                callback.onSuccess();
            } else {
                callback.onFailed();
            }
        }
    }
}
