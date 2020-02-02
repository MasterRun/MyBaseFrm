package com.jsongo.im.imui.listener.internal

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import cn.jiguang.imui.chatinput.listener.OnMenuClickListener
import cn.jiguang.imui.chatinput.model.FileItem
import com.jsongo.core.widget.RxToast
import com.jsongo.im.imui.listener.IChatOperationListenerCallback
import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @author jsongo
 * @date 2019/3/10 15:25
 *
 * // TODO: 2020/2/1 权限申请待优化（使用注解）
 */
class MenuClickListener(private val callback: IChatOperationListenerCallback) :
    OnMenuClickListener {
    private val rxPermissions: RxPermissions = RxPermissions(callback.activity)

    override fun onSendTextMessage(input: CharSequence?): Boolean = callback.sendTextCallback(input)

    override fun onSendFiles(list: List<FileItem>?) = callback.sendFilesCallback(list)

    override fun switchToMicrophoneMode(): Boolean {
        callback.chatView.scrollToBottom()
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val disposable = rxPermissions.request(*permissions)
            .subscribe { granted: Boolean ->
                if (!granted) {
                    RxToast.error("无法使用录音机")
                }
            }
        callback.compositeDisposable.add(disposable)
        return true
    }

    override fun switchToGalleryMode(): Boolean {
        callback.chatView.scrollToBottom()
        val permissions = arrayOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        val disposable =
            rxPermissions.request(*permissions).subscribe()
        callback.compositeDisposable.add(disposable)
        // If you call updateData, select photo view will try to update data(Last update over 30 seconds.)
        callback.chatView.chatInputView.selectPhotoView.updateData()
        return true
    }

    override fun switchToCameraMode(): Boolean {
        callback.chatView.scrollToBottom()
        val perms = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
        var granted = true
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(
                    callback.activity,
                    perm
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                granted = false
                break
            }
        }
        if (!granted) {
            val disposable =
                rxPermissions.request(*perms).subscribe()
            callback.compositeDisposable.add(disposable)
            return false
        }
        return true
    }

    override fun switchToEmojiMode(): Boolean {
        callback.chatView.scrollToBottom()
        return true
    }

}