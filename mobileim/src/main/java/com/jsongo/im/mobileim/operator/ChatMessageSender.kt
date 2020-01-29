package com.jsongo.im.mobileim.operator

import android.content.Context
import android.text.TextUtils
import com.jsongo.core.network.ApiManager
import com.jsongo.core.common.CommonCallBack
import com.jsongo.core.util.LogcatUtil
import com.jsongo.core.util.StringCompress
import com.jsongo.im.MobileIM
import com.jsongo.im.bean.Message
import com.jsongo.im.bean.UdpData
import com.jsongo.im.bean.UdpDataType
import net.openmob.mobileimsdk.android.conf.ConfigEntity
import net.openmob.mobileimsdk.android.core.LocalUDPDataSender
import net.openmob.mobileimsdk.android.core.LocalUDPDataSender.SendLoginDataAsync

/**
 * @author jsongo
 * @date 2019/3/10 21:54
 */
object ChatMessageSender {

    private val gson = ApiManager.getGson()

    /**
     * 登陆IM
     *
     * @param chatId
     * @param token
     * @param callback 回调标识登陆的数据发送是否成功
     */
    fun loginIM(chatId: String?, token: String?, callback: CommonCallBack?) {
        val loginTask = object : SendLoginDataAsync(MobileIM.context, chatId, token) {
            override fun fireAfterSendLogin(code: Int) {
                if (code == 0) {
                    LogcatUtil.e("登录数据发送成功！, serverip:" + ConfigEntity.serverIP)
                    callback?.success(null)
                } else {
                    LogcatUtil.e("登录数据发送失败。错误码是：" + code + "！, serverip:" + ConfigEntity.serverIP)
                    callback?.failed(code, "登录失败！", null)
                }
            }
        }
        loginTask.execute()
    }

    /**
     * 发送消息
     */
    fun sendMessageAsync(message: Message?, toUserId: String, callback: CommonCallBack?) {
        if (TextUtils.isEmpty(toUserId) || message == null) {
            return
        }
        val messageUdpData = UdpData(
            message.sender_id, toUserId,
            UdpDataType.Message.ordinal, message
        )
        val msg = gson.toJson(messageUdpData, UdpData.messageUdpDataType)
        LogcatUtil.d("ChatMessageSender send: $msg")
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
        private val callback: CommonCallBack?
    ) : LocalUDPDataSender.SendCommonDataAsync(context, dataContentWidthStr, toUserId) {

        override fun onPostExecute(code: Int?) {
            if (code == 0) {
                callback?.success(null)
            } else {
                callback?.failed(code ?: -1, "消息发送失败！", null)
            }
        }
    }
}
