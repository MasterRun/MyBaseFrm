package com.jsongo.mobileim.operator

import android.content.Context
import android.text.TextUtils
import com.jsongo.core.network.ApiManager
import com.jsongo.core.util.StringCompress
import com.jsongo.mobileim.MobileIM
import com.jsongo.mobileim.bean.Message
import com.jsongo.mobileim.bean.UdpData
import com.jsongo.mobileim.bean.UdpDataType
import com.safframework.log.L
import net.openmob.mobileimsdk.android.core.LocalUDPDataSender

/**
 * @author jsongo
 * @date 2019/3/10 21:54
 */
object ChatMessageSender {

    private val gson = ApiManager.getGson()

    fun sendMessageAsync(message: Message?, toUserId: String, callback: SendCallback) {
        if (TextUtils.isEmpty(toUserId) || message == null) {
            return
        }
        val messageUdpData = UdpData(
            message.sender_id, toUserId,
            UdpDataType.Message.ordinal, message
        )
        val msg = gson.toJson(messageUdpData, UdpData.messageUdpDataType)
        L.d("ChatMessageSender send: $msg")
        val gzip = StringCompress.gzip(msg)
        SenderAsync(MobileIM.context, gzip, toUserId, callback).execute()
    }
    /*
    public static void sendToServerAsync(Conversation conversation, SendCallback callback) {
        String toUserId = "0";
        UdpData<Conversation> conversationUdpData = new UdpData<>(MobileIM.user.getChat_id(),
                toUserId, UdpDataType.Conversation.ordinal(), conversation);
        String conv = gson.toJson(conversationUdpData, UdpData.Companion.getConversationUdpData());
        new SenderAsync(MobileIM.context, conv, toUserId, callback).execute();
    }*/

    class SenderAsync(
        context: Context,
        dataContentWidthStr: String,
        toUserId: String,
        private val callback: SendCallback
    ) : LocalUDPDataSender.SendCommonDataAsync(context, dataContentWidthStr, toUserId) {

        override fun onPostExecute(code: Int?) {
            if (code == 0) {
                callback.onSuccess()
            } else {
                callback.onFailed()
            }
        }
    }
}
