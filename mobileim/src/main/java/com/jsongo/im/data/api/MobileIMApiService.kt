package com.jsongo.im.data.api

import com.jsongo.core.bean.DataWrapper
import com.jsongo.im.bean.Conversation
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author ： jsongo
 * @date ： 2020/1/18 17:16
 * @desc :
 */
interface MobileIMApiService {

    /**
     * 获取会话列表
     */
    @POST("conversation/conversations")
    suspend fun getConversations(): DataWrapper<List<Conversation>?>

    /**
     * 获取某会话信息
     */
    @POST("conversation/conversation")
    @FormUrlEncoded
    suspend fun getConversation(
        @Field("aim_chat_id") aim_chat_id: String
    ): DataWrapper<Conversation?>

    /**
     *
     */
    @POST("conversation/getByConvId")
    @FormUrlEncoded
    suspend fun getConversationByConvid(
        @Field("conv_id") conv_id: String
    ): DataWrapper<Conversation?>

    /**
     * 设置某会话已读
     */
    @POST("conversation/setread")
    @FormUrlEncoded
    suspend fun setConversationRead(
        @Field("conv_id") conv_id: String
    ): DataWrapper<Boolean?>

    /**
     * 获取未读数量
     */
    @POST("conversation/unreadcount")
    @FormUrlEncoded
    suspend fun getUnreadMessageCount(
        @Field("conv_id") conv_id: String
    ): DataWrapper<Int?>

}