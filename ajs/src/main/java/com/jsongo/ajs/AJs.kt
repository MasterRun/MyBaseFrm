package com.jsongo.ajs

import android.annotation.SuppressLint
import android.content.Context
import com.tencent.smtt.sdk.QbSdk

/**
 * author ： jsongo
 * createtime ： 2019/7/23 9:26
 * desc :
 */
@SuppressLint("StaticFieldLeak")
object AJs {
    lateinit var context: Context
    fun init(context: Context) {
        this.context = context

        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(context, null)
    }
}