package com.jsongo.core.bean

import com.google.gson.JsonObject
import com.jsongo.core.BaseCore
import com.jsongo.core.R

/**
 * @author ： jsongo
 * @date ： 2019/11/10 0:13
 * @desc : 数据的基本包装类型
 */

abstract class Data<T>(code: Int, message: String, data: T)

data class DataWrapper<T>(
    var code: Int,
    var message: String,
    var data: T
) : Data<T>(code, message, data)

data class ErrorDataWrapper(
    var message: String,
    var code: Int = -1,
    var data: JsonObject = JsonObject()
) : Data<JsonObject>(code, message, data) {

    companion object {
        //默认错误实例
        val DEFAULT = ErrorDataWrapper(
            BaseCore.context.getString(R.string.empty_view_error_detail)
        )
    }
}

/**
 * 将String作为错误信息包装为 ErrorDataWrapper
 */
fun String?.toErrorDataWrapper(): ErrorDataWrapper =
    if (this.isNullOrEmpty()) {
        ErrorDataWrapper.DEFAULT
    } else {
        ErrorDataWrapper(this)
    }