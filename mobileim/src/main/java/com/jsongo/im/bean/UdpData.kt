package com.jsongo.im.bean

import com.google.gson.reflect.TypeToken
import java.io.Serializable

/**
 * @author  jsongo
 * @date 2019/3/11 13:24
 */
data class UdpData<T>
constructor(
    var senderId: String,
    var receiverId: String,
    var type: Int,
    var content: T
) : Serializable {
    companion object {
        val messageUdpDataType = object : TypeToken<UdpData<Message>>() {}.type
        val serverEventUdpDataType = object : TypeToken<UdpData<ServerEvent>>() {}.type
        val anyUdpDataType = object : TypeToken<UdpData<Any?>>() {}.type
    }
}

enum class UdpDataType {
    //消息
    Message,
    //会话
    Conversation,
    //服务端数据
    ServerData,
}