package com.jsongo.ui

import android.content.Context

/**
 * @author  jsongo
 * @date 2019/8/1 22:08
 * @desc
 */
object BaseUI {
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context
    }
}