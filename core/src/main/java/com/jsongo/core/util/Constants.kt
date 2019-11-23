package com.jsongo.core.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午4:08
 * @desc : 常量
 */

val gson: Gson = Gson()

val strHashMapType = object : TypeToken<HashMap<String, String>>() {
}.type

const val URL_REG = "[a-zA-z]+://[^\\s]*"

const val PRE_ANDROID_ASSET = "file:///android_asset"

const val SRT_HTTP = "http"

const val DATE_STR_FORMAT_STR = "yyyy-MM-dd HH:mm:ss"