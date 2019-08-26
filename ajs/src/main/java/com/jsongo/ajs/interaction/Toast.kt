package com.jsongo.ajs.interaction

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.vondear.rxtool.view.RxToast

/**
 * @author  jsongo
 * @date 2019/6/20 15:24
 * @desc RxToast封装api
 */
object Toast {
    @JvmStatic
    fun error(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(1, params["text"].toString(), jsWebLoader, bridgeWebView, callback)
    }

    @JvmStatic
    fun warning(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(2, params["text"].toString(), jsWebLoader, bridgeWebView, callback)
    }

    @JvmStatic
    fun info(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(3, params["text"].toString(), jsWebLoader, bridgeWebView, callback)
    }

    @JvmStatic
    fun normal(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(4, params["text"].toString(), jsWebLoader, bridgeWebView, callback)
    }

    @JvmStatic
    fun success(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        toast(5, params["text"].toString(), jsWebLoader, bridgeWebView, callback)
    }

    @JvmStatic
    fun toast(
        type: Int,
        text: String?,
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
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