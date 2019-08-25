package com.jsongo.ajs.interaction

import com.github.lzyzsd.jsbridge.CallBackFunction
import com.jsongo.ajs.Util
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
        function: CallBackFunction
    ) {
        val key = params["key"] ?: ""
        val value = params["value"] ?: ""
        if (key.isEmpty()) {
            val map = hashMapOf(Pair("result", "0"))
            val result = Util.gson.toJson(map)
            function.onCallBack(result)
        } else {
            CommonDbOpenHelper.setKeyValue(key, value)
            val map = hashMapOf(Pair("result", "1"))
            val result = Util.gson.toJson(map)
            function.onCallBack(result)
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
        function: CallBackFunction
    ) {
        val key = params["key"] ?: ""
        if (key.isEmpty()) {
            val map = hashMapOf(Pair("result", "0"))
            val result = Util.gson.toJson(map)
            function.onCallBack(result)
        } else {
            val value = CommonDbOpenHelper.getValue(key) ?: ""
            val map = hashMapOf(Pair("result", "1"), Pair("value", value))
            val result = Util.gson.toJson(map)
            function.onCallBack(result)
        }
    }
}