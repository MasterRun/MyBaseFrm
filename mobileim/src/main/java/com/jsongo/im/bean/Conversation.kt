package com.jsongo.im.bean

import com.jsongo.core_mini.util.StringUtil
import java.io.Serializable

/**
 * @author  jsongo
 * @date 2019/3/9 15:06
 */
data class Conversation
constructor(
    @Transient
    var id: Long = 0,
    var conv_id: String = StringUtil.genUUID(),
    var chat_id1: String = "",
    var chat_id2: String = ""
) : Serializable {
    /**
     * 未读消息数
     */
    var unreadCount: Int = 0
    /**
     * 上一条消息
     */
    var lastMessage: Message? = null
    /**
     * 头像
     */
    var avatar: String = ""
    /**
     * 会话名
     */
    var convName: String = ""
}