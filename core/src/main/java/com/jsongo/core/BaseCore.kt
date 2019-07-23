package com.jsongo.core

import android.annotation.SuppressLint
import android.content.Context
import android.support.multidex.MultiDex
import com.jsongo.core.widget.ImagePreview.ZoomImageLoader
import com.previewlibrary.ZoomMediaLoader
import com.vondear.rxtool.RxTool

/**
 * author ： jsongo
 * createtime ： 2019/7/23 8:58
 * desc :
 */
@SuppressLint("StaticFieldLeak")
object BaseCore {

    lateinit var context: Context

    fun init() {
        ZoomMediaLoader.getInstance().init(ZoomImageLoader())
        RxTool.init(context)
    }

    fun attachBaseContext(context: Context) {
        this.context = context
        MultiDex.install(context)
    }

    var isDebug: Boolean = false
}
