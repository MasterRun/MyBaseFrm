package com.jsongo.mybasefrm.data.api

import com.jsongo.core.bean.DataWrapper
import com.jsongo.mybasefrm.bean.User
import retrofit2.http.*

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

    @GET("user/info")
    suspend fun getUserInfo(
        @Query("user_guid") userguid: String?
    ): DataWrapper<User?>
}