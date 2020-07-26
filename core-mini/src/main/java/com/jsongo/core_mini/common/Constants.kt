package com.jsongo.core_mini.common

import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.jsongo.core_mini.CoreMini
import java.lang.reflect.Type
import java.sql.Timestamp
import java.util.*

/**
 * @author ： jsongo
 * @date ： 19-10-19 下午4:08
 * @desc : 常量
 */

const val KEY_ACTIVITY_FORE = "KEY_ACTIVITY_FORE"

//file uri的auth值
val FILE_PROVIDER_AUTH: String = CoreMini.context.packageName + ".core.fileprovider"

val strHashMapType = object : TypeToken<HashMap<String, String>>() {
}.type

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