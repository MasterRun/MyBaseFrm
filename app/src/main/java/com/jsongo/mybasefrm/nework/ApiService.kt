package com.jsongo.mybasefrm.nework

import com.google.gson.JsonObject
import retrofit2.http.GET

/**
 * author ： jsongo
 * createtime ： 2019/7/25 18:04
 * desc :
 */
interface ApiService {

    @GET("today")
    suspend fun dailyGank(): JsonObject

    @GET("userauth/types")
    suspend fun authtypes(): JsonObject
}