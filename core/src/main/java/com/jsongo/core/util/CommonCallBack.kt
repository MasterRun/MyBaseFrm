package com.jsongo.core.util

/**
 * @author ： jsongo
 * @date ： 2019/12/29 13:50
 * @desc : 通用回调
 */
interface CommonCallBack {
    fun success(data: Map<String, Any?>?)
    fun failed(code: Int, msg: String, throwable: Throwable?)
}