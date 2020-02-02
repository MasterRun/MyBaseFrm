package com.jsongo.im.imui.listener.internal

import android.text.format.DateFormat
import cn.jiguang.imui.chatinput.listener.RecordVoiceListener
import com.jsongo.im.imui.listener.IChatOperationListenerCallback
import com.jsongo.im.util.ChatFileConf
import java.io.File
import java.util.*

/**
 * @author jsongo
 * @date 2019/3/10 15:36
 */
class RecordVoiceListener(
    private val callback: IChatOperationListenerCallback
) : RecordVoiceListener {

    override fun onStartRecord() { // set voice file path, after recording, audio file will save here
        val path = ChatFileConf.audioPath
        val destDir = File(path)
        if (!destDir.exists()) {
            destDir.mkdirs()
        }

        callback.chatView.setRecordVoiceFile(
            destDir.path,
            DateFormat.format(
                "yyyy-MM-dd-hhmmss",
                Calendar.getInstance(Locale.CHINA)
            ).toString() + ""
        )
    }

    override fun onFinishRecord(voiceFile: File, duration: Int) =
        callback.recordVoiceComplete(voiceFile, duration)

    override fun onCancelRecord() {
        // 录音取消
    }

    /**
     * In preview record voice layout, fires when click cancel button
     * Add since chatinput v0.7.3
     */
    override fun onPreviewCancel() {}

    /**
     * In preview record voice layout, fires when click send button
     * Add since chatinput v0.7.3
     */
    override fun onPreviewSend() {}

}