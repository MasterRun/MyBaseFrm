package com.jsongo.mybasefrm.data.api

import com.google.gson.JsonObject
import com.jsongo.core.bean.DataWrapper
import retrofit2.http.GET

/**
 * author ： jsongo
 * createtime ： 2019/7/25 18:04
 * desc :
 */
interface ApiService {

    @GET("today")
    suspend fun dailyGank(): DataWrapper<JsonObject>

    @GET("userauth/types")
    suspend fun authtypes(): JsonObject
}