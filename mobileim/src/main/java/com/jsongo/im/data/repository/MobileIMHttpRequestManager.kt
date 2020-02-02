package com.jsongo.im.data.repository

import com.jsongo.core.network.ApiManager
import com.jsongo.core.network.checkResult
import com.jsongo.im.bean.Conversation
import com.jsongo.im.bean.Message
import com.jsongo.im.data.api.MobileIMApiService

/**
 * @author ： jsongo
 * @date ： 2020/1/18 18:53
 * @desc :
 */
object MobileIMHttpRequestManager : IMobileIMRemoteRequest {

    @Throws
    override suspend fun getConversations(): List<Conversation> =
        checkResult {
            ApiManager.createApiService(MobileIMApiService::class.java).getConversations()
        }

    @Throws
    override suspend fun getConversation(aim_chat_id: String): Conversation =
        checkResult {
            ApiManager.createApiService(MobileIMApiService::class.java).getConversation(aim_chat_id)
        }

    @Throws
    override suspend fun getConversationByConvid(conv_id: String): Conversation =
        checkResult {
            ApiManager.createApiService(MobileIMApiService::class.java)
                .getConversationByConvid(conv_id)
        }

    @Throws
    override suspend fun setConversationRead(conv_id: String): Boolean =
        checkResult {
            ApiManager.createApiService(MobileIMApiService::class.java).setConversationRead(conv_id)
        }

    @Throws
    override suspend fun getUnreadMessageCount(conv_id: String): Int =
        checkResult {
            ApiManager.createApiService(MobileIMApiService::class.java)
                .getUnreadMessageCount(conv_id)
        }

    @Throws
    override suspend fun getMessages(conv_id: String, pageIndex: Int): List<Message> =
        checkResult {
            ApiManager.createApiService(MobileIMApiService::class.java)
                .getMessages(conv_id, pageIndex)
        }
}