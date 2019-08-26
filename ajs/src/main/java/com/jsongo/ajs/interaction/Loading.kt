package com.jsongo.ajs.interaction

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader

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
        callback: AjsCallback
    ) {
        jsWebLoader.loadingDialog.show()
        callback.success()
    }

    /**
     * hide
     */
    @JvmStatic
    fun hide(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        jsWebLoader.loadingDialog.dismiss()
        callback.success()
    }

    /**
     * cancelable
     */
    @JvmStatic
    fun cancelable(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val cancelable = !params["cancelable"].equals("false")
        jsWebLoader.loadingDialog.setCancelable(cancelable)
        callback.success()
    }
}