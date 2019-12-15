package com.jsongo.mybasefrm.data.api

import com.jsongo.core.bean.DataWrapper
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * author ： jsongo
 * createtime ： 2019/7/25 18:04
 * desc :
 */
interface ApiService {

    @POST("user/check")
    @FormUrlEncoded
    suspend fun checkUser(
        @Field("username") userName: String?,
        @Field("password") password: String?
    ): DataWrapper<String?>
}