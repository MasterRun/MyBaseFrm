package com.jsongo.ajs.helper

import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.interaction.LongDataTransfer
import com.jsongo.ajs.lzyzsd_jsbridge.CallBackFunction
import com.jsongo.ajs.util.Util
import java.util.*

/**
 * author ： jsongo
 * createtime ： 19-8-26 下午11:19
 * desc : api 回调方法
 */
class AjsCallback(val function: CallBackFunction) {

    companion object {
        /**
         * 回调的键
         */
        const val CALLBACK_KEY_CODE = "result"
        const val CALLBACK_KEY_MESSAGE = "message"

        /**
         * 回调的默认值
         */
        const val SUCCESS_CODE = 1
        const val FAIL_CODE = 0
        const val LONG_DATA_CODE = 9999

        /**
         * 回调的默认消息
         */
        const val SUCCESS_MESSAGE = "success"
        const val FAIL_MESSAGE = "failed"
        const val LONG_DATA_MESSAGE = "data to long , please use LongDataTransfer "

    }

    fun success(
        vararg data: Pair<String, Any>,
        resultCode: Int = SUCCESS_CODE,
        message: String = SUCCESS_MESSAGE
    ) {
        callback(resultCode, message, data.toMap().toMutableMap())
    }

    fun success(
        data: MutableMap<String, Any> = hashMapOf(),
        resultCode: Int = SUCCESS_CODE,
        message: String = SUCCESS_MESSAGE
    ) {
        callback(resultCode, message, data)
    }

    fun failure(e: Exception) {
        failure(hashMapOf(Pair("error", e.cause?.message ?: "")), message = "exception occured")
    }

    fun failure(
        data: HashMap<String, Any> = hashMapOf(),
        resultCode: Int = FAIL_CODE,
        message: String = FAIL_MESSAGE
    ) {
        callback(resultCode, message, data)
    }

    fun callback(
        resultCode: Int,
        message: String,
        data: MutableMap<String, Any>
    ) {
        data[CALLBACK_KEY_CODE] = "$resultCode"
        data[CALLBACK_KEY_MESSAGE] = message
        val result = Util.gson.toJson(data)
        //数据未超出最大长度，直接传输
        if (result.length < AJs.context.resources.getInteger(R.integer.long_data_max_length)) {
            function.onCallBack(result)
        } else {
            //加到长数据集合
            val key = UUID.randomUUID().toString()
            LongDataTransfer.longDataMap[key] = result

            //回调长数据
            data.clear()
            data[CALLBACK_KEY_CODE] = LONG_DATA_CODE
            data[CALLBACK_KEY_MESSAGE] = LONG_DATA_MESSAGE
            //长数据的key
            data["dataKey"] = key
            data["length"] = result.length
            function.onCallBack(Util.gson.toJson(data))
        }
    }
}