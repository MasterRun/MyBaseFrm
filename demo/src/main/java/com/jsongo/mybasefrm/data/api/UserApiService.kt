package com.jsongo.mybasefrm.data.api

import com.jsongo.core.bean.DataWrapper
import com.jsongo.mybasefrm.bean.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * author ： jsongo
 * createtime ： 2019/7/25 18:04
 * desc :
 */
interface UserApiService {

    @POST("user/check")
    @FormUrlEncoded
    suspend fun checkUser(
        @Field("account") account: String?,
        @Field("password") password: String?
    ): DataWrapper<String?>

    @GET("user/info")
    suspend fun getUserInfo(): DataWrapper<User?>
}