package com.jsongo.mobileim.bean

import java.io.Serializable
import java.util.*

/**
 * @author  jsongo
 * @date 2019/3/9 15:06
 */
data class Conversation
constructor(
    @Transient
    var id: Long = 0,
    var conv_id: String = UUID.randomUUID().toString(),
    var chat_id1: String = "",
    var chat_id2: String = ""
) : Serializable {
    var lastMessage: Message? = null
    var avatar: String = ""
    var convName: String = ""
}