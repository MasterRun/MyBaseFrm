package com.jsongo.core.util

import com.jsongo.core.constant.gson

/**
 * @author ： jsongo
 * @date ： 2019/12/15 18:21
 * @desc : 扩展方法
 */

fun <T> String?.toGsonBean(clazz: Class<T>): T? {
    val obj = try {
        gson.fromJson(this, clazz)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
    return obj
}