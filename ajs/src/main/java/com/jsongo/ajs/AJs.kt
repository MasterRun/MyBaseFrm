package com.jsongo.ajs

import android.annotation.SuppressLint
import android.content.Context
import com.jsongo.ajs.webloader.DefaultWebLoader
import com.safframework.log.L
import com.tencent.smtt.sdk.QbSdk
import com.vondear.rxfeature.activity.ActivityScanerCode
import com.vondear.rxfeature.module.scaner.OnRxScanerListener
import com.vondear.rxtool.RxActivityTool

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
        QbSdk.initX5Environment(context, null)

        //设置扫描信息回调
        ActivityScanerCode.setScanerListener(object : OnRxScanerListener {
            override fun onSuccess(type: String?, result: com.google.zxing.Result?) {
                DefaultWebLoader.load(result?.text ?: "")
                RxActivityTool.finishActivity()
                RxActivityTool.currentActivity().finish()
            }

            override fun onFail(type: String?, message: String?) {
                L.e(message)
            }
        })
    }
}