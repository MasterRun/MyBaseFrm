package com.jsongo.core.network

/**
 * @author  jsongo
 * @date 2019/3/26 17:24
 */
interface ApiCallback<T> {

    fun onSuccess(t: T)

    fun onFailed(code: Int, msg: String, obj: Any?)
}