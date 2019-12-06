package com.jsongo.mobileim

import android.annotation.SuppressLint
import android.content.Context
import com.jsongo.mobileim.core.MobileIMConfig

/**
 * @author ： jsongo
 * @date ： 2019/11/12 18:04
 * @desc :
 */
object MobileIM {
    lateinit var context: Context

    fun init(context: Context) {
        this.context = context

        MobileIMConfig.init(context)
    }

}