package com.jsongo.ajs.interaction

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.core.mvp.base.BaseActivity

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
        val hostActivity = jsWebLoader.activity
        if (hostActivity is BaseActivity) {
            hostActivity.loadingDialog.show()
            callback.success()
        } else {
            callback.failure(message = "hostActivity is not BaseActivity")
        }
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
        val hostActivity = jsWebLoader.activity
        if (hostActivity is BaseActivity) {
            hostActivity.loadingDialog.dismiss()
            callback.success()
        } else {
            callback.failure(message = "hostActivity is not BaseActivity")
        }
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
        val hostActivity = jsWebLoader.activity
        if (hostActivity is BaseActivity) {
            val cancelable = !params["cancelable"].equals("false")
            hostActivity.loadingDialog.setCancelable(cancelable)
            hostActivity.loadingDialog.dismiss()
            callback.success()
        } else {
            callback.failure(message = "hostActivity is not BaseActivity")
        }
    }
}