package com.jsongo.im.imui

import cn.jiguang.imui.commons.models.IMessage
import com.google.gson.JsonObject
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.util.toGsonBean
import com.jsongo.im.bean.ChatMessage
import com.jsongo.im.bean.Message

/**
 * @author  jsongo
 * @date 2019/3/9 16:01
 */
object MessageUtil {

    fun messagesToChatMessages(messages: List<Message>): List<ChatMessage> {
        return ArrayList<ChatMessage>().apply {
            for (message in messages) {
                val chatMessage = ChatMessage(message)
                CommonDbOpenHelper.getValue(CommonDbKeys.USER_INFO)
                val userguid = CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID)
                if (chatMessage.fromUser.id == userguid) {
                    chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED)
                } else {
                    chatMessage.setMessageStatus(IMessage.MessageStatus.RECEIVE_SUCCEED)
                }
                add(chatMessage)
            }
        }
    }

    fun getUserJsonInfo() = CommonDbOpenHelper.getValue(CommonDbKeys.USER_INFO)
        .toGsonBean(JsonObject::class.java)

    fun getDisplayMessageContent(message: Message?): String {
        if (message == null) {
            return ""
        }
        return when (message.type) {
            Message.TYPE_IMAGE -> "[图片]"
            Message.TYPE_AUDIO -> "[语音]"
            Message.TYPE_FILE -> "[文件]"
            Message.TYPE_VIDEO -> "[视频]"
            Message.TYPE_TEXT -> message.content
            else -> message.content
        }
    }

    fun chatMessageTypeToMessageType(type: Int) =
        when (type) {
            IMessage.MessageType.RECEIVE_TEXT.ordinal -> Message.TYPE_TEXT
            IMessage.MessageType.SEND_TEXT.ordinal -> Message.TYPE_TEXT
            IMessage.MessageType.RECEIVE_IMAGE.ordinal -> Message.TYPE_IMAGE
            IMessage.MessageType.SEND_IMAGE.ordinal -> Message.TYPE_IMAGE
            IMessage.MessageType.RECEIVE_VOICE.ordinal -> Message.TYPE_AUDIO
            IMessage.MessageType.SEND_VOICE.ordinal -> Message.TYPE_AUDIO
            IMessage.MessageType.RECEIVE_VIDEO.ordinal -> Message.TYPE_VIDEO
            IMessage.MessageType.SEND_VIDEO.ordinal -> Message.TYPE_VIDEO
            IMessage.MessageType.RECEIVE_FILE.ordinal -> Message.TYPE_FILE
            IMessage.MessageType.SEND_FILE.ordinal -> Message.TYPE_FILE
            else -> -1
        }

}