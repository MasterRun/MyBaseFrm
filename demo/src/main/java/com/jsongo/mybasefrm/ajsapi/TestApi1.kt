package com.jsongo.mybasefrm.ajsapi

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.AJsWebView
import com.jsongo.annotation.anno.AjsApi
import com.jsongo.core_mini.widget.RxToast

/**
 * author ： jsongo
 * createtime ： 19-9-4 下午11:25
 * desc :
 */
object TestApi1 {
    @AjsApi(prefix = "custom1", methodName = "myToast")
    @JvmStatic
    fun toast(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
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