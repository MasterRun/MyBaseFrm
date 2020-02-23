package com.jsongo.core.constant

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.sql.Timestamp
import java.util.*

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午4:08
 * @desc : 常量
 */

val gson: Gson = GsonBuilder()
    .registerTypeAdapter(Date::class.java, DateSerializer())
    .registerTypeAdapter(Date::class.java, DateDeserializer())
    .create()

class DateSerializer : JsonSerializer<Date> {
    override fun serialize(
        src: Date,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return JsonPrimitive(src.time)
    }
}

class DateDeserializer : JsonDeserializer<Date> {
    @Throws(JsonParseException::class)
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date {
        val asLong = json.asLong
        return Timestamp(asLong)
    }
}

val strHashMapType = object : TypeToken<HashMap<String, String>>() {
}.type

const val URL_REG = "[a-zA-z]+://[^\\s]*"

const val PRE_ANDROID_ASSET = "file:///android_asset"

const val SRT_HTTP = "http"

const val DATE_STR_FORMAT_STR = "yyyy-MM-dd HH:mm:ss"

const val KEY_ACTIVITY_FORE = "KEY_ACTIVITY_FORE"