package com.jsongo.core.common

/**
 * @author ： jsongo
 * @date ： 2019/12/29 13:50
 * @desc : 通用回调
 */
interface CommonCallback<T> {
    fun success(data: T)
    fun failed(code: Int, msg: String, throwable: Throwable?)
}

interface MapCallBack : CommonCallback<Map<String, Any?>?>