package com.jsongo.im.imui

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import cn.jiguang.imui.commons.models.IMessage
import com.jsongo.core.arch.mvvm.BaseViewModel
import com.jsongo.core.common.CommonCallback
import com.jsongo.core.common.RxBus
import com.jsongo.core.constant.CommonDbKeys
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.network.NetFailedException
import com.jsongo.core.network.ServerAddr
import com.jsongo.core.util.LogcatUtil
import com.jsongo.core.widget.RxToast
import com.jsongo.im.bean.ChatMessage
import com.jsongo.im.bean.Conversation
import com.jsongo.im.bean.Message
import com.jsongo.im.data.repository.ChatFileRequestManager
import com.jsongo.im.data.repository.ChatFileType
import com.jsongo.im.data.repository.MobileIMHttpRequestManager
import com.jsongo.im.util.ChatFileConf
import com.jsongo.im.util.MobileIMMessageSign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

/**
 * @author ： jsongo
 * @date ： 2020/1/30 15:40
 * @desc : 聊天页面的ViewModel
 */
class ChatViewModel : BaseViewModel() {

    /**
     * 当前聊天会话的id
     */
    var convid = ""
    /**
     * 聊天对方用户guid
     */
    var aimUserguid: String = ""

    /**
     * 当前聊天的对方用户
     */
    val conversation = MutableLiveData<Conversation>()

    val imagePathList = ArrayList<String>()
    val imgMsgIdList = ArrayList<String>()

    /**
     * 新发送需要显示的message
     */
    val newSendMessage = MutableLiveData<ChatMessage>()
    /**
     * 需要更新的消息
     */
    val updateMessage = MutableLiveData<ChatMessage>()

    /**
     * 历史消息
     */
    val historyMessages = MutableLiveData<List<ChatMessage>>()

    /**
     * 加载消息的页数
     */
    val pageIndex = MutableLiveData<Int>()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onStart() {
        getConv()
    }

    fun getConv() {
        mainScope.launch {
            try {
                val conv: Conversation
                if (convid.isNotEmpty()) {
                    conv = MobileIMHttpRequestManager.getConversationByConvid(convid)
                    val localUserguid = CommonDbOpenHelper.getValue(CommonDbKeys.USER_GUID)
                    aimUserguid =
                        if (localUserguid == conv.chat_id1) conv.chat_id2 else conv.chat_id1
                } else if (aimUserguid.isNotEmpty()) {
                    conv = MobileIMHttpRequestManager.getConversation(aimUserguid)
                } else {
                    throw  java.lang.Exception("参数错误！")
                }
                convid = conv.conv_id
                conversation.value = conv
            } catch (e: Exception) {
                RxToast.error(e.message ?: "参数错误！")
                e.printStackTrace()
                return@launch
            }
            //注册监听
            registerEvent()
            //加载最近的消息
            pageIndex.value = 1
        }
    }

    /**
     * 获取消息
     */
    fun getMessages(pageIndex: Int) {
        if (convid.isEmpty()) {
            return
        }
        mainScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val messages = MobileIMHttpRequestManager.getMessages(convid, pageIndex)
                    val chatMessages = MessageUtil.messagesToChatMessages(messages)
                    messages.filter {
                        it.type == Message.TYPE_IMAGE
                    }.forEach {
                        //加到图片消息集合中
                        imgMsgIdList.add(0, it.msg_id)
                        imagePathList.add(0, ServerAddr.SERVER_ADDRESS + it.content)
                    }
                    //todo 如果是本地视/音频 直接使用
                    /*switch (message.getType()) {
                      case Message.TYPE_AUDIO:
                          String content = message.getContent();
                          String[] split = content.split("/");
                          String filename = split[split.length - 1];
                          File audioFile = new File(SubUrl.getExternal_audio_path(), filename);
                          if (audioFile.exists()) {
                              message.setFilePath(audioFile.getAbsolutePath());
                          } else {

                          }
                          break;
                      case Message.TYPE_VIDEO:
                          break;
                      default:
                    }*/
                    //设置消息已读
                    MobileIMHttpRequestManager.setConversationRead(convid)
                    historyMessages.postValue(chatMessages)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 监听收到udp消息
     */
    private fun registerEvent() {
        class DownloadFileCallback(
            var message: Message, var chatMessage: ChatMessage
        ) : CommonCallback<File> {
            override fun success(t: File) {

                message.filePath = t.absolutePath
                chatMessage.mediaFilePath = t.absolutePath
                try {
                    MediaPlayer().apply {
                        setDataSource(t.path)
                        prepare()
                        chatMessage.duration = (duration / 1000).toLong()
                    }.release()
                    chatMessage.setMessageStatus(IMessage.MessageStatus.RECEIVE_SUCCEED)
                    updateMessage.postValue(chatMessage)
                } catch (e: IOException) {
                    e.printStackTrace()
                    failed(0, "media error", e)
                }
            }

            override fun failed(code: Int, msg: String, throwable: Throwable?) {
                chatMessage.setMessageStatus(IMessage.MessageStatus.RECEIVE_FAILED)
                updateMessage.postValue(chatMessage)
            }
        }

        val disposable = RxBus.toFlowable()
            .filter { event ->
                event.code == MobileIMMessageSign.IM_RECEIVE_MESSAGE && event.data is Message
            }
            .map<ChatMessage> { event ->
                val message = event.data as Message
                LogcatUtil.e(message.toString())
                var chatMessage: ChatMessage? = null
                if (message.sender_id == aimUserguid) {
                    chatMessage = ChatMessage(message)
                    if (message.type == Message.TYPE_IMAGE) {
                        imgMsgIdList.add(message.msg_id)
                        imagePathList.add(ServerAddr.SERVER_ADDRESS + message.content)
                    } else if (message.type == Message.TYPE_AUDIO) {
                        chatMessage.setMessageStatus(IMessage.MessageStatus.RECEIVE_GOING)
                        val downloadFileCallback = DownloadFileCallback(message, chatMessage)
                        downloadFile(
                            ServerAddr.SERVER_ADDRESS + message.content,
                            ChatFileConf.audioPath,
                            downloadFileCallback
                        )
                    } else if (message.type == Message.TYPE_VIDEO) {
                        chatMessage.setMessageStatus(IMessage.MessageStatus.RECEIVE_GOING)
                        val downloadFileCallback = DownloadFileCallback(message, chatMessage)
                        downloadFile(
                            ServerAddr.SERVER_ADDRESS + message.content,
                            ChatFileConf.videoPath,
                            downloadFileCallback
                        )
                    }
                }
                chatMessage
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ chatMessage ->
                newSendMessage.value = chatMessage
            }, { t ->
                LogcatUtil.e("", t)
            })
        addDisposable(disposable)
    }

    /**
     * 上传聊天文件
     */
    fun uploadChatFile(fileType: ChatFileType, file: File, callback: CommonCallback<String>) {
        mainScope.launch {
            try {
                val uploadFile = ChatFileRequestManager.uploadFile(fileType, file)
                callback.success(uploadFile)
            } catch (e: NetFailedException) {
                e.printStackTrace()
                callback.failed(-1, "", e)
            }
        }
    }

    /**
     * 下载文件
     */
    fun downloadFile(url: String, savePath: String, callback: CommonCallback<File>) {
        mainScope.launch {
            try {
                val downloadFile = ChatFileRequestManager.downloadFile(url, savePath)
                callback.success(downloadFile)
            } catch (e: Exception) {
                callback.failed(-1, "", e)
            }
        }
    }

    /**
     * 移除无用的聊天文件
     */
    fun removeUnusedFile(files: List<String>) {
        mainScope.launch {
            try {
                ChatFileRequestManager.removeUnusedFiles(files)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeUnusedFile(filePath: String) {
        removeUnusedFile(arrayListOf(filePath))
    }

    open class EventProxy(protected val viewModel: ChatViewModel) {

        /**
         * 刷新（加载历史消息）
         */
        fun refresh() {
            viewModel.pageIndex.apply {
                value = (value ?: 0) + 1
            }
        }

    }

}