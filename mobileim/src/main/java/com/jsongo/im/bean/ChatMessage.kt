package com.jsongo.im.bean

import android.text.TextUtils
import cn.jiguang.imui.commons.models.IMessage
import cn.jiguang.imui.commons.models.IUser
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.network.ServerAddr
import com.jsongo.core.util.relativeTime

/**
 * @author jsongo
 * @date 2019/3/8 19:54
 */
class ChatMessage
constructor(
    val message: Message
) : IMessage {

    private var fromUser: IUser =
        ChatUser(message.sender_id, message.senderName, message.senderAvatar)

    //转换type
    private var type: Int = messageTypeToChatMessageType(message)

    private var messageStatus: IMessage.MessageStatus = IMessage.MessageStatus.SEND_GOING
    private var text: String = ""
    private var duration: Long = 0
    private var progress: String = ""
    private var mediaFilePath: String = ""
    private var extras: HashMap<String, String>? = null

    init {
        //获取text 和 mediaFilePath类型
        when (message.type) {
            Message.TYPE_TEXT -> text = message.content
            Message.TYPE_IMAGE -> {
                mediaFilePath = if (TextUtils.isEmpty(message.content)) {
                    message.filePath
                } else {
                    ServerAddr.SERVER_ADDRESS + message.content
                }
            }
            Message.TYPE_AUDIO -> mediaFilePath = message.filePath

            Message.TYPE_VIDEO -> mediaFilePath = message.filePath

            // TODO: 2019/3/16 其他消息类型
        }
    }

    override fun getMsgId(): String = message.msg_id


    override fun getFromUser(): IUser = fromUser

    override fun getMessageStatus(): IMessage.MessageStatus? = messageStatus

    fun setMessageStatus(status: IMessage.MessageStatus) {
        messageStatus = status
    }

    override fun getDuration(): Long = duration

    fun setDuration(duration: Long) {
        this.duration = duration
    }

    override fun getExtras(): HashMap<String, String>? = extras

/*    fun setExtras(extras: HashMap<String, String>) {
        this.extras = extras
    }*/

    override fun getType(): Int = type

    fun setType(type: Int) {
        this.type = type
    }

    override fun getText(): String = text

/*    fun setText(text: String) {
        this.text = text
    }*/

    override fun getProgress(): String = progress

/*    fun setProgress(progress: String) {
        this.progress = progress
    }*/

    override fun getMediaFilePath(): String = mediaFilePath

    fun setMediaFilePath(mediaFilePath: String) {
        this.mediaFilePath = mediaFilePath
    }

    override fun getTimeString(): String = message.send_time.relativeTime()

    /**
     * 消息类型转换
     */
    fun messageTypeToChatMessageType(message: Message): Int {
        val userguid = CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID) ?: ""
        return when (message.type) {
            Message.TYPE_TEXT ->
                if (userguid == message.sender_id) {
                    IMessage.MessageType.SEND_TEXT.ordinal
                } else {
                    IMessage.MessageType.RECEIVE_TEXT.ordinal
                }
            Message.TYPE_AUDIO ->
                if (userguid == message.sender_id) {
                    IMessage.MessageType.SEND_VOICE.ordinal
                } else {
                    IMessage.MessageType.RECEIVE_VOICE.ordinal
                }
            Message.TYPE_FILE ->
                if (userguid == message.sender_id) {
                    IMessage.MessageType.SEND_FILE.ordinal
                } else {
                    IMessage.MessageType.RECEIVE_FILE.ordinal
                }
            Message.TYPE_IMAGE ->
                if (userguid == message.sender_id) {
                    IMessage.MessageType.SEND_IMAGE.ordinal
                } else {
                    IMessage.MessageType.RECEIVE_IMAGE.ordinal
                }
            Message.TYPE_VIDEO ->
                if (userguid == message.sender_id) {
                    IMessage.MessageType.SEND_VIDEO.ordinal
                } else {
                    IMessage.MessageType.RECEIVE_VIDEO.ordinal
                }
            else -> IMessage.MessageType.SEND_TEXT.ordinal

        }
    }
}
