package com.jsongo.mobileim.operator

import com.google.gson.JsonSyntaxException
import com.jsongo.core.network.ApiManager
import com.jsongo.core.util.BusEvent
import com.jsongo.core.util.RxBus
import com.jsongo.core.util.StringCompress
import com.jsongo.mobileim.bean.Message
import com.jsongo.mobileim.bean.ServerEvent
import com.jsongo.mobileim.bean.UdpData
import com.jsongo.mobileim.bean.UdpDataType
import com.jsongo.mobileim.util.MobileIMMessageSign
import com.safframework.log.L

/**
 * @author jsongo
 * @date 2019/3/10 22:22
 * C2C S2C消息接受回调
 */
class ChatMessageReceiver {

    private val gson = ApiManager.getGson()

    fun onReceive(fromUserid: String, dataContent: String) {
        try {
            val udpJsonData = StringCompress.gunzip(dataContent)
            L.d(ChatMessageReceiver::class.java.name, "收到来自用户" + fromUserid + "的消息:" + udpJsonData)
            val udpData = gson.fromJson(udpJsonData, UdpData::class.java)
            if (udpData.type == UdpDataType.Message.ordinal) {
                onReceiveMessage(udpJsonData)
            } else if ("0" == fromUserid && udpData.type == UdpDataType.ServerData.ordinal) {
                onReceiveServerEvent(udpJsonData)
            } else {
                L.e("未解析的数据！ fromUserid:$fromUserid  data : $udpData")
            }
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            L.e("数据解析错误！ fromUserid:$fromUserid")
        }
        L.d("onReceive finish")
    }

    fun onError(errorCode: Int, errorMsg: String) {
        L.e("收到服务端错误消息，errorCode=$errorCode, errorMsg=$errorMsg")
    }

    private fun onReceiveMessage(jsonData: String) {
        val messageUdpData = gson.fromJson<UdpData<Message>>(jsonData, UdpData.messageUdpDataType)
        val busEvent = BusEvent(MobileIMMessageSign.IM_RECEIVE_MESSAGE, "收到即时通讯消息", messageUdpData.content)
        RxBus.post(busEvent)
        L.d("rxbus post message : " + messageUdpData.content)
    }

    private fun onReceiveServerEvent(jsonData: String) {
        val serverEventUdpData =
            gson.fromJson<UdpData<ServerEvent>>(jsonData, UdpData.serverEventUdpDataType)
        val busEvent =
            BusEvent(MobileIMMessageSign.IM_RECEIVE_SERVER_DATA, "收到服务端消息", serverEventUdpData.content)
        RxBus.post(busEvent)
        L.d("rxbus post serverenevt: " + serverEventUdpData.content)
    }
}
