package com.jsongo.ajs.interaction

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.AJsWebView
import com.jsongo.core.db.CommonDbOpenHelper

/**
 * author ： jsongo
 * createtime ： 19-8-25 下午11:35
 * desc : sqlite缓存
 */
object Cache {

    /**
     * 存值
     */
    @JvmStatic
    fun put(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val key = params["key"] ?: ""
        val value = params["value"] ?: ""
        if (key.isEmpty()) {
            callback.failure()
        } else {
            CommonDbOpenHelper.setKeyValue(key, value)
            callback.success()
        }
    }

    /**
     * 取值
     */
    @JvmStatic
    fun get(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val key = params["key"] ?: ""
        if (key.isEmpty()) {
            callback.failure()
        } else {
            val value = CommonDbOpenHelper.getValue(key) ?: ""
            callback.success(Pair("value", value))
        }
    }
}