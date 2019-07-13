package com.jsongo.mybasefrm.jsloader.interaction

import com.github.lzyzsd.jsbridge.CallBackFunction
import com.jsongo.mybasefrm.jsloader.AJsWebLoader
import com.jsongo.mybasefrm.jsloader.jsbridge.BridgeWebView

/**
 * author ： jsongo
 * createtime ： 2019/7/12 15:21
 * desc : 加载dialog
 */
object Loading {
    /**
     * show
     */
    @JvmStatic
    fun show(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        jsWebLoader.loadingDialog?.show()
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * hide
     */
    @JvmStatic
    fun hide(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        jsWebLoader.loadingDialog?.dismiss()
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * cancelable
     */
    @JvmStatic
    fun cancelable(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val cancelable = !params["cancelable"].equals("false")
        jsWebLoader.loadingDialog?.setCancelable(cancelable)
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }
}