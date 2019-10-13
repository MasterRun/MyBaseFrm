package com.jsongo.ui

import android.annotation.SuppressLint
import android.content.Context
import com.jsongo.ui.component.imagepreview.ZoomImageLoader
import com.previewlibrary.ZoomMediaLoader

/**
 * @author  jsongo
 * @date 2019/8/1 22:08
 * @desc
 */
@SuppressLint("StaticFieldLeak")
object BaseUI {
    lateinit var context: Context
    fun init(context: Context) {
        this.context = context
        ZoomMediaLoader.getInstance().init(ZoomImageLoader())
    }
}