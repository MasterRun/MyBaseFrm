package com.jsongo.mybasefrm.nework

import com.google.gson.JsonObject
import io.reactivex.Observable
import retrofit2.http.GET

/**
 * author ： jsongo
 * createtime ： 2019/7/25 18:04
 * desc :
 */
interface ApiService {

    @GET("today")
    fun dailyGank(): Observable<JsonObject>

    @GET("today")
    suspend fun dailyGank2(): JsonObject
}