package com.jsongo.ajs.interaction

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
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
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
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
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
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