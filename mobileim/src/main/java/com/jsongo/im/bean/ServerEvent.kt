package com.jsongo.im.bean

import java.io.Serializable
import java.sql.Timestamp

/**
 * @author  jsongo
 * @date 2019/4/30 22:53
 * @desc 服务端客户端消息事件
 *
 */
data class ServerEvent
constructor(
    var id: Long,
    //chat id
    var to_user: String,
    // type
    var type: Int,
    // data
    var data: String,
    //发送时间
    var send_time: Timestamp,
    // 是否已发送
    @Transient
    var sended: Boolean

) : Serializable