package com.jsongo.core_mini.util

import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.util.*

/**
 * @author ： jsongo
 * @date ： 2020/1/29 16:36
 * @desc :
 */
object StringUtil {

    fun genUUID() = UUID.randomUUID().toString()//.replace("-", "")
}

fun String.formatJsonStr(): String {
    var formatStr = this
    try {
        val jsonTokener = JSONTokener(this)
        val any = jsonTokener.nextValue()
        if (any is JSONArray) {
            formatStr = any.toString(1)
        } else if (any is JSONObject) {
            formatStr = any.toString(1)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return formatStr
}