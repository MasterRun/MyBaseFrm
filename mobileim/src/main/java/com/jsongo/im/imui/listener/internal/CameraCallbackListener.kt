package com.jsongo.im.imui.listener.internal

import cn.jiguang.imui.chatinput.listener.OnCameraCallbackListener
import com.jsongo.core_mini.widget.RxToast
import com.jsongo.im.imui.listener.IChatOperationListenerCallback

/**
 * @author jsongo
 * @date 2019/3/10 15:38
 */
class CameraCallbackListener(
    private val callback: IChatOperationListenerCallback
) : OnCameraCallbackListener {

    override fun onTakePictureCompleted(photoPath: String) =
        callback.takePicComplete(photoPath)

    override fun onStartVideoRecord() {
        RxToast.info("onStartVideoRecord")
    }

    /**
     * 视频录制完成后触发，
     * 发送视频是在onSendFiles方法中
     * [com.jsongo.im.imui.listener.MenuClickListener.onSendFiles]
     *
     * @param videoPath
     */
    override fun onFinishVideoRecord(videoPath: String) {}

    override fun onCancelVideoRecord() {
        RxToast.info("onCancelVideoRecord")
    }
}