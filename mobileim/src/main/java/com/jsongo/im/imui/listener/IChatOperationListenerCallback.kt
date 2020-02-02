package com.jsongo.im.imui.listener

import androidx.fragment.app.FragmentActivity
import cn.jiguang.imui.chatinput.model.FileItem
import com.jsongo.im.imui.ChatView
import io.reactivex.disposables.CompositeDisposable
import java.io.File

/**
 * @author ： jsongo
 * @date ： 2020/2/1 13:39
 * @desc :
 */
interface IChatOperationListenerCallback {

    val chatView: ChatView
    val activity: FragmentActivity
    val compositeDisposable: CompositeDisposable

    fun takePicComplete(photoPath: String)
    fun recordVoiceComplete(voiceFile: File, duration: Int)
    fun sendTextCallback(messageText: CharSequence?): Boolean
    fun sendFilesCallback(files: List<FileItem>?)
}