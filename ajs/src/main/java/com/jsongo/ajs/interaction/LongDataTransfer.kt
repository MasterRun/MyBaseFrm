package com.jsongo.ajs.interaction

import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.helper.Util
import com.jsongo.ajs.widget.AJsWebView

/**
 * @author ： jsongo
 * @date ： 2019/11/10 17:09
 * @desc : 长数据传输 api
 */
object LongDataTransfer {

    /**
     * 长数据map ，key使用uuid，value为数据值
     */
    val longDataMap = HashMap<String, String>()

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
        val key = params["key"]
        //非法key
        if (key.isNullOrEmpty() || longDataMap.containsKey(key).not()) {
            callback.failure(message = "invalid key :" + key)
            return
        }
        val data = longDataMap[key]
        //空数据
        if (data.isNullOrEmpty()) {
            callback.failure(message = "data is empty")
            return
        }
        val startIndex = (params["startIndex"] ?: "0").toInt()
        //startIndex无效
        if (startIndex >= data.length || startIndex < 0) {
            callback.failure(message = "invalid startIndex ")
            return
        }
        var length = (params["length"] ?: "0").toInt()
        //限制传输长度
        if (length < AJs.context.resources.getInteger(R.integer.long_data_part_min_length) || length >= AJs.context.resources.getInteger(
                R.integer.long_data_max_length
            )
        ) {
            //默认传输数据长度
            length = AJs.context.resources.getInteger(R.integer.long_data_part_length)
        }
        //截取数据
        val partData = data.substring(startIndex, startIndex + length)
        //直接调用，不会再AjsCallback中校验长度
        val map = mutableMapOf(
            Pair(AjsCallback.CALLBACK_KEY_CODE, AjsCallback.SUCCESS_CODE.toString()),
            Pair(AjsCallback.CALLBACK_KEY_MESSAGE, AjsCallback.SUCCESS_MESSAGE),
            Pair("partData", partData)
        )
        callback.function.onCallBack(Util.gson.toJson(map))
    }

    /**
     * 取值完成,移除此值
     */
    @JvmStatic
    fun complete(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val key = params["key"]
        if (key.isNullOrEmpty() || longDataMap.containsKey(key).not()) {
            callback.failure(message = "invalid key :" + key)
            return
        }
        longDataMap.remove(key)
        callback.success()
    }
}