package com.jsongo.ajs.interaction

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.AJsWebView
import com.jsongo.core_mini.widget.RxToast

/**
 * @author  jsongo
 * @date 2019/6/20 15:24
 * @desc RxToast封装api
 */
object Toast {
    @JvmStatic
    fun error(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(1, params["text"].toString(), callback)
    }

    @JvmStatic
    fun warning(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(2, params["text"].toString(), callback)
    }

    @JvmStatic
    fun info(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(3, params["text"].toString(), callback)
    }

    @JvmStatic
    fun normal(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(4, params["text"].toString(), callback)
    }

    @JvmStatic
    fun success(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(5, params["text"].toString(), callback)
    }

    @JvmStatic
    fun toast(
        type: Int,
        text: String?,
        callback: AjsCallback
    ) {
        if (text.isNullOrEmpty()) {
            callback.failure()
            return
        }
        when (type) {
            1 -> RxToast.error(text)
            2 -> RxToast.warning(text)
            3 -> RxToast.info(text)
            4 -> RxToast.normal(text)
            5 -> RxToast.success(text)
        }
        callback.success()
    }
}