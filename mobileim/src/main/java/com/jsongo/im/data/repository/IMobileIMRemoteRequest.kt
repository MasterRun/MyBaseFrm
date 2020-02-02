/*
 * Copyright 2018-2019 KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jsongo.im.data.repository

import com.jsongo.im.bean.Conversation
import com.jsongo.im.bean.Message

/**
 * @author jsongo
 * @date 2020/1/18 18:47
 * @desc
 */
interface IMobileIMRemoteRequest {

    @Throws
    suspend fun getConversations(): List<Conversation>

    @Throws
    suspend fun getConversation(aim_chat_id: String): Conversation

    @Throws
    suspend fun getConversationByConvid(conv_id: String): Conversation

    @Throws
    suspend fun setConversationRead(conv_id: String): Boolean

    @Throws
    suspend fun getUnreadMessageCount(conv_id: String): Int

    @Throws
    suspend fun getMessages(conv_id: String, pageIndex: Int): List<Message>
}