package com.jsongo.mobileim.bean

import java.io.Serializable
import java.sql.Timestamp
import java.util.*

/**
 * @author  jsongo
 * @date 2019/3/9 15:09
 */
data class Message
constructor(
    @Transient
    var id: Long = 0,
    var msg_id: String = UUID.randomUUID().toString(),
    var sender_id: String = "",
    var type: Int = 1,
    var content: String = "",//文本内容或是文件的url
    var send_time: Timestamp = Timestamp(System.currentTimeMillis()),
    var conv_id: String = "",
    var have_read: Boolean = false
) : Serializable {
    @Transient
    var fileLocalPath: String = ""//文件在本地的地址

    companion object {
        const val TYPE_TEXT = 1
        const val TYPE_IMAGE = 2
        const val TYPE_AUDIO = 3
        const val TYPE_VIDEO = 4
        const val TYPE_FILE = 5
        const val TYPE_OTHER = 100
    }
}
