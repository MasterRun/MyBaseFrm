package com.jsongo.ajs

import android.annotation.SuppressLint
import android.content.Context
import com.safframework.log.L
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

        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。

        val cb = object : QbSdk.PreInitCallback {

            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                L.d("tencentx5 init", " onViewInitFinished is $arg0")
            }

            override fun onCoreInitFinished() {
                L.d("tencentx5 init", " onCoreInitFinished")
            }
        }
        //x5内核初始化接口
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(context.applicationContext, cb)
    }
}