package com.jsongo.ajs.interaction

import com.github.lzyzsd.jsbridge.CallBackFunction
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.vondear.rxtool.view.RxToast

/**
 * @author  jsongo
 * @date 2019/6/20 15:24
 * @desc
 */
object Toast {
    @JvmStatic
    fun error(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        toast(1, params["text"].toString(), jsWebLoader, bridgeWebView, function)
    }

    @JvmStatic
    fun warning(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        toast(2, params["text"].toString(), jsWebLoader, bridgeWebView, function)
    }

    @JvmStatic
    fun info(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        toast(3, params["text"].toString(), jsWebLoader, bridgeWebView, function)
    }

    @JvmStatic
    fun normal(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        toast(4, params["text"].toString(), jsWebLoader, bridgeWebView, function)
    }

    @JvmStatic
    fun success(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        toast(5, params["text"].toString(), jsWebLoader, bridgeWebView, function)
    }

    @JvmStatic
    fun toast(
        type: Int,
        text: String?,
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        function: CallBackFunction
    ) {
        if (text.isNullOrEmpty()) {
            val map = hashMapOf(Pair("result", "0"))
            val result = Util.gson.toJson(map)
            function.onCallBack(result)
            return
        }
        when (type) {
            1 -> RxToast.error(text)
            2 -> RxToast.warning(text)
            3 -> RxToast.info(text)
            4 -> RxToast.normal(text)
            5 -> RxToast.success(text)
        }
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }
}