package com.jsongo.im.imui.listener

import cn.jiguang.imui.chatinput.model.FileItem
import cn.jiguang.imui.chatinput.model.VideoItem
import cn.jiguang.imui.commons.models.IMessage
import com.jsongo.core.common.CommonCallback
import com.jsongo.core.common.MapCallBack
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.network.ServerAddr
import com.jsongo.core.util.DateUtil
import com.jsongo.core.util.LogcatUtil
import com.jsongo.core.util.StringUtil
import com.jsongo.core.widget.RxToast
import com.jsongo.im.bean.ChatMessage
import com.jsongo.im.bean.Message
import com.jsongo.im.data.repository.ChatFileType
import com.jsongo.im.imui.ChatViewModel
import com.jsongo.im.imui.MessageUtil
import com.jsongo.im.mobileim.operator.ChatMessageSender
import java.io.File

abstract class ChatOpeListenerCallback(
    protected val viewModel: ChatViewModel
) : IChatOperationListenerCallback {

    override val compositeDisposable = viewModel.compositeDisposable

    override fun recordVoiceComplete(voiceFile: File, duration: Int) {
        val userguid = CommonDbOpenHelper.getValue(
            CommonDbKeys.USER_GUID
        ) ?: ""
        val message = Message(
            0, StringUtil.genUUID(),
            userguid, Message.TYPE_AUDIO, "",
            DateUtil.currentTimeStamp(), viewModel.convid,
            false
        )
        MessageUtil.getUserJsonInfo()?.apply {
            message.senderName = get("username")?.asString ?: ""
            message.senderAvatar = get("photo_url")?.asString ?: ""
        }
        message.filePath = voiceFile.absolutePath
        val chatMessage = ChatMessage(message)
        chatMessage.duration = duration.toLong()
        viewModel.uploadChatFile(
            ChatFileType.Audio,
            voiceFile,
            object : CommonCallback<String> {
                override fun success(url: String) {
                    message.content = url
                    val sendMessageAsyncCallback: MapCallBack = object :
                        MapCallBack {
                        override fun success(data: Map<String, Any?>?) {
                            chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED)
                            viewModel.updateMessage.postValue(chatMessage)
                        }

                        override fun failed(code: Int, msg: String, throwable: Throwable?) {
                            viewModel.removeUnusedFile(url)
                            chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                            viewModel.updateMessage.postValue(chatMessage)
                        }
                    }
                    ChatMessageSender.sendMessageAsync(
                        message,
                        viewModel.aimUserguid,
                        sendMessageAsyncCallback
                    )
                }

                override fun failed(code: Int, msg: String, throwable: Throwable?) {
                    chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                    viewModel.updateMessage.postValue(chatMessage)
                }
            })
        viewModel.newSendMessage.postValue(chatMessage)
    }

    override fun takePicComplete(photoPath: String) {
        val userguid = CommonDbOpenHelper.getValue(
            CommonDbKeys.USER_GUID
        ) ?: ""
        val message = Message(
            0, StringUtil.genUUID(), userguid,
            Message.TYPE_IMAGE, "",
            DateUtil.currentTimeStamp(), viewModel.convid,
            false
        )
        MessageUtil.getUserJsonInfo()?.apply {
            message.senderName = get("username")?.asString ?: ""
            message.senderAvatar = get("photo_url")?.asString ?: ""
        }
        message.type = Message.TYPE_IMAGE
        message.filePath = photoPath
        val chatMessage = ChatMessage(message)
        viewModel.uploadChatFile(
            ChatFileType.Image,
            File(photoPath),
            object : CommonCallback<String> {
                override fun success(url: String) {
                    message.filePath = url
                    message.content = url
                    chatMessage.mediaFilePath = ServerAddr.SERVER_ADDRESS + url

                    val sendMsgCallback = object :
                        MapCallBack {
                        override fun success(data: Map<String, Any?>?) {
                            viewModel.imagePathList.add(ServerAddr.SERVER_ADDRESS + url)
                            viewModel.imgMsgIdList.add(message.msg_id)
                            chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED)
                            viewModel.updateMessage.postValue(chatMessage)
                        }

                        override fun failed(code: Int, msg: String, throwable: Throwable?) {
                            viewModel.removeUnusedFile(url)
                            chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                            viewModel.updateMessage.postValue(chatMessage)
                        }
                    }
                    ChatMessageSender.sendMessageAsync(
                        message,
                        viewModel.aimUserguid,
                        sendMsgCallback
                    )
                }

                override fun failed(code: Int, msg: String, throwable: Throwable?) {
                    chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                    viewModel.updateMessage.postValue(chatMessage)
                }
            })
        viewModel.newSendMessage.postValue(chatMessage)
    }

    override fun sendTextCallback(messageText: CharSequence?): Boolean {
        if (messageText.isNullOrEmpty()) {
            return false
        }
        if (messageText.length > 140) {
            RxToast.warning("暂不支持发送大文本！")
            return false
        }
        var userguid = CommonDbOpenHelper.getValue(
            CommonDbKeys.USER_GUID
        )
        if (userguid == null) {
            userguid = ""
        }
        val message = Message(
            0,
            StringUtil.genUUID(),
            userguid,
            Message.TYPE_TEXT,
            messageText.toString(),
            DateUtil.currentTimeStamp(),
            viewModel.convid,
            false
        )
        MessageUtil.getUserJsonInfo()?.apply {
            message.senderName = get("username")?.asString ?: ""
            message.senderAvatar = get("photo_url")?.asString ?: ""
        }
        val chatMessage = ChatMessage(message)
        viewModel.newSendMessage.postValue(chatMessage)
        val sendMsgCallback: MapCallBack = object :
            MapCallBack {
            override fun success(data: Map<String, Any?>?) {
                LogcatUtil.e("发送成功")
                chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED)
                viewModel.updateMessage.postValue(chatMessage)
            }

            override fun failed(
                code: Int,
                msg: String,
                throwable: Throwable?
            ) {
                LogcatUtil.e(msg)
                chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                viewModel.updateMessage.postValue(chatMessage)
            }
        }
        ChatMessageSender.sendMessageAsync(
            message,
            viewModel.aimUserguid,
            sendMsgCallback
        )
        return true
    }

    override fun sendFilesCallback(files: List<FileItem>?) {
        if (files == null || files.isEmpty()) {
            return
        }
        for (item in files) {
            val msgId = StringUtil.genUUID()
            var userguid =
                CommonDbOpenHelper.getValue(
                    CommonDbKeys.USER_GUID
                )
            if (userguid == null) {
                userguid = ""
            }
            val message = Message(
                0,
                msgId,
                userguid,
                Message.TYPE_IMAGE,
                "",
                DateUtil.currentTimeStamp(),
                viewModel.convid,
                false
            )
            MessageUtil.getUserJsonInfo()?.apply {
                message.senderName = get("username")?.asString ?: ""
                message.senderAvatar = get("photo_url")?.asString ?: ""
            }
            message.filePath = item.filePath
            val chatMessage = ChatMessage(message)
            if (item.type == FileItem.Type.Image) {
                message.type = Message.TYPE_IMAGE
                chatMessage.type = IMessage.MessageType.SEND_IMAGE.ordinal
                viewModel.uploadChatFile(
                    ChatFileType.Image,
                    File(item.filePath),
                    object : CommonCallback<String> {
                        override fun success(url: String) {
                            message.filePath = url
                            message.content = url
                            chatMessage.mediaFilePath = ServerAddr.SERVER_ADDRESS + url
                            val sendMsgCallback: MapCallBack = object :
                                MapCallBack {
                                override fun success(data: Map<String, Any?>?) {
                                    viewModel.imagePathList.add(ServerAddr.SERVER_ADDRESS + url)
                                    viewModel.imgMsgIdList.add(msgId)
                                    chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED)
                                    viewModel.updateMessage.postValue(chatMessage)
                                }

                                override fun failed(
                                    code: Int,
                                    msg: String,
                                    throwable: Throwable?
                                ) {
                                    viewModel.removeUnusedFile(url)
                                    chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                                    viewModel.updateMessage.postValue(chatMessage)
                                }
                            }
                            ChatMessageSender.sendMessageAsync(
                                message,
                                viewModel.aimUserguid,
                                sendMsgCallback
                            )
                        }

                        override fun failed(
                            code: Int,
                            msg: String,
                            throwable: Throwable?
                        ) {
                            chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                            viewModel.updateMessage.postValue(chatMessage)
                        }
                    })
            } else if (item.type == FileItem.Type.Video) {
                message.type = Message.TYPE_VIDEO
                chatMessage.type = IMessage.MessageType.SEND_VIDEO.ordinal
                chatMessage.duration = (item as VideoItem).duration
                viewModel.uploadChatFile(
                    ChatFileType.Video,
                    File(item.getFilePath()),
                    object : CommonCallback<String> {
                        override fun success(url: String) {
                            message.content = url
                            val sendMsgCallback: MapCallBack = object :
                                MapCallBack {
                                override fun success(data: Map<String, Any?>?) {
                                    chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_SUCCEED)
                                    viewModel.updateMessage.postValue(chatMessage)
                                }

                                override fun failed(
                                    code: Int,
                                    msg: String,
                                    throwable: Throwable?
                                ) {
                                    viewModel.removeUnusedFile(url)
                                    chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                                    viewModel.updateMessage.postValue(chatMessage)
                                }
                            }
                            ChatMessageSender.sendMessageAsync(
                                message,
                                viewModel.aimUserguid,
                                sendMsgCallback
                            )
                        }

                        override fun failed(
                            code: Int,
                            msg: String,
                            throwable: Throwable?
                        ) {
                            chatMessage.setMessageStatus(IMessage.MessageStatus.SEND_FAILED)
                            viewModel.updateMessage.postValue(chatMessage)
                        }
                    })
            } else {
                throw RuntimeException("Invalid FileItem type. Must be Type.Image or Type.Video")
            }
            viewModel.newSendMessage.postValue(chatMessage)
        }
    }
}