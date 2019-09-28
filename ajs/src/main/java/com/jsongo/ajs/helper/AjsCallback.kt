package com.jsongo.ajs.helper

import com.jsongo.ajs.lzyzsd_jsbridge.CallBackFunction

/**
 * author ： jsongo
 * createtime ： 19-8-26 下午11:19
 * desc : api 回调方法
 */
class AjsCallback(private val function: CallBackFunction) {

    fun success(
        vararg data: Pair<String, Any>,
        resultCode: Int = 1,
        message: String = "success"
    ) {
        callback(resultCode, message, data.toMap().toMutableMap())
    }

    fun success(
        data: MutableMap<String, Any> = hashMapOf(),
        resultCode: Int = 1,
        message: String = "success"
    ) {
        callback(resultCode, message, data)
    }

    fun failure(
        e: Exception
    ) {
        failure(hashMapOf(Pair("error", e.cause?.message ?: "")), message = "exception occured")
    }

    fun failure(
        data: HashMap<String, Any> = hashMapOf(),
        resultCode: Int = 0,
        message: String = "failed"
    ) {
        callback(resultCode, message, data)
    }

    fun callback(
        resultCode: Int,
        message: String,
        data: MutableMap<String, Any>
    ) {
        data["result"] = "$resultCode"
        data["message"] = message
        val result = Util.gson.toJson(data)
        function.onCallBack(result)
    }
}