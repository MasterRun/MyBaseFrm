package com.jsongo.core.bean

import com.jsongo.core.BaseCore
import com.jsongo.core.R

/**
 * @author ： jsongo
 * @date ： 2019/11/10 0:13
 * @desc : 数据的基本包装类型
 */
open class DataWrapper<T>(
    open var data: T?,
    open val code: Int = 1,
    open var message: String = ""
) {
    fun toErrorDataWrapper() = ErrorDataWrapper(message, code, data)
}

data class ErrorDataWrapper(
    override var message: String,
    override val code: Int = -1,
    override var data: Any? = null
) : DataWrapper<Any>(data, code, message) {

    companion object {
        //默认错误实例
        val DEFAULT: ErrorDataWrapper by lazy {
            ErrorDataWrapper(
                BaseCore.context.getString(R.string.empty_view_error_detail)
            )
        }
    }
}

data class ErrorPluginWrapper(
    override var message: String,
    override val code: Int = -1,
    override var data: MutableMap<String, Any?>? = null
) : DataWrapper<MutableMap<String, Any?>>(data, code, message) {

    companion object {
        //默认错误实例
        val DEFAULT: ErrorPluginWrapper by lazy {
            ErrorPluginWrapper(
                "组件调用出错"
            )
        }
    }
}

/**
 * 将String作为错误信息包装为 ErrorDataWrapper
 */
fun String?.toErrorDataWrapper(any: Any? = null): ErrorDataWrapper =
    if (this.isNullOrEmpty()) {
        ErrorDataWrapper.DEFAULT
    } else {
        ErrorDataWrapper(this)
    }.apply {
        data = any
    }