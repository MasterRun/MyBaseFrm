package com.jsongo.ajs.interaction

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.AJsWebView
import com.jsongo.core.arch.BaseActivityWrapper

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
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = ajsWebViewHost.hostActivity
        if (hostActivity is BaseActivityWrapper) {
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
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = ajsWebViewHost.hostActivity
        if (hostActivity is BaseActivityWrapper) {
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
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = ajsWebViewHost.hostActivity
        if (hostActivity is BaseActivityWrapper) {
            val cancelable = !params["cancelable"].equals("false")
            hostActivity.loadingDialog.setCancelable(cancelable)
            hostActivity.loadingDialog.dismiss()
            callback.success()
        } else {
            callback.failure(message = "hostActivity is not BaseActivity")
        }
    }
}