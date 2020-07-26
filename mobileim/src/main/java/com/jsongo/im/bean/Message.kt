package com.jsongo.im.bean

import com.jsongo.core_mini.util.StringUtil
import java.io.Serializable
import java.sql.Timestamp

/**
 * @author  jsongo
 * @date 2019/3/9 15:09
 */
data class Message
constructor(
    @Transient
    var id: Long = 0,
    var msg_id: String = StringUtil.genUUID(),
    var sender_id: String = "",
    var type: Int = 1,
    var content: String = "",//文本内容或是文件的url
    var send_time: Timestamp = Timestamp(System.currentTimeMillis()),
    var conv_id: String = "",
    var have_read: Boolean = false
) : Serializable {

    /**
     * 发送者头像
     */
    var senderAvatar: String = ""
    /**
     * 发送者昵称
     */
    var senderName: String = ""

    /**
     * 文件在本地的地址
     */
    @Transient
    var filePath: String = ""

    companion object {
        const val TYPE_TEXT = 1
        const val TYPE_IMAGE = 2
        const val TYPE_AUDIO = 3
        const val TYPE_VIDEO = 4
        const val TYPE_FILE = 5
        const val TYPE_OTHER = 100
    }
}
