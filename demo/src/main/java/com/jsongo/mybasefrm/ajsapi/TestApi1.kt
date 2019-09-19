package com.jsongo.mybasefrm.ajsapi

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.annotation.anno.AjsApi
import com.vondear.rxtool.view.RxToast

/**
 * author ： jsongo
 * createtime ： 19-9-4 下午11:25
 * desc :
 */
object TestApi1 {
    @AjsApi(prefix = "custom1", methodName = "myToast")
    @JvmStatic
    fun toast(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val text = params["text"].toString()
        if (text.isEmpty()) {
            callback.failure()
            return
        }
        RxToast.error(text)
        callback.success()
    }
}