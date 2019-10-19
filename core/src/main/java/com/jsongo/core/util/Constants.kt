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