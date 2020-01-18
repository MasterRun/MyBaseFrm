package com.jsongo.mobileim.data.api

import com.jsongo.core.bean.DataWrapper
import com.jsongo.mobileim.bean.Conversation
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * @author ： jsongo
 * @date ： 2020/1/18 17:16
 * @desc :
 */
interface MobileIMApiService {

    @POST("conversation/conversations")
    suspend fun getConversations(): DataWrapper<List<Conversation>?>

    @POST("conversation/conversation")
    @FormUrlEncoded
    suspend fun getConversation(
        @Field("aim_chat_id") aim_chat_id: String
    ): DataWrapper<List<Conversation>?>


    @POST("conversation/getByConvId")
    @FormUrlEncoded
    suspend fun getConversationByConvid(
        @Field("conv_id") conv_id: String
    ): DataWrapper<List<Conversation>?>


    @POST("conversation/setread")
    @FormUrlEncoded
    suspend fun setConversationRead(
        @Field("conv_id") conv_id: String
    ): DataWrapper<List<Conversation>?>


    @POST("conversation/unreadcount")
    @FormUrlEncoded
    suspend fun getUnreadMessageCount(
        @Field("conv_id") conv_id: String
    ): DataWrapper<List<Conversation>?>

}