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
object TestApi2 {
    @AjsApi(prefix = "custom2")
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

/*
class TestApi3{
    companion object {

        @AjsApi(prefix = "custom3")
        @JvmStatic
        fun toast(
            ajsWebViewHost: AjsWebViewHost,
            aJsWebView: AJsWebView,
            params: Map<String, String>, callback: AjsCallback
        ) {
            val text = params["text"]
            if (TextUtils.isEmpty(text)) {
                callback.failure(HashMap(), 0, "")
                return
            }
            RxToast.error(text!!)
            callback.success(HashMap(), 1, "success")
        }
    }
}*/
