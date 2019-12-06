package com.jsongo.ui.enhance

import android.content.Context
import com.jsongo.ui.enhance.qqfaceview.QQFaceManager
import com.qmuiteam.qmui.qqface.QMUIQQFaceCompiler

/**
 * @author ： jsongo
 * @date ： 2019/12/6 16:07
 * @desc :
 */
object UIEnhance {

    lateinit var context: Context

    fun init(context: Context) {
        this.context = context

        //QQFace设置默认的FaceManager
        QMUIQQFaceCompiler.setDefaultQQFaceManager(QQFaceManager.getInstance())
    }

}