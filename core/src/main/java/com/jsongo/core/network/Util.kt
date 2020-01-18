package com.jsongo.core.network

import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.bean.toErrorDataWrapper

/**
 * @author ： jsongo
 * @date ： 2020/1/18 19:18
 * @desc :
 */
/**
 * 检查网络请求结果
 */
suspend fun <T> checkResult(netRequestFunc: suspend () -> DataWrapper<T?>): T {
    try {
        val dataWrapper = netRequestFunc()
        val data = dataWrapper.data
        if (dataWrapper.code > 0 && data != null) {
            if (data is String && data.isEmpty()) {
                throw NetFailedException(dataWrapper.toErrorDataWrapper())
            }
            return data
        } else {
            throw NetFailedException(dataWrapper.toErrorDataWrapper())
        }
    } catch (e: NetFailedException) {
        throw e
    } catch (e: Exception) {
        throw NetFailedException(e.message.toErrorDataWrapper())
    }
}