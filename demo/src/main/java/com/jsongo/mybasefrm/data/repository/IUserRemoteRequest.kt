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

package com.jsongo.mybasefrm.data.repository

import com.jsongo.mybasefrm.bean.User


/**
 * @author jsongo
 * @date 2019/11/9 23:35
 * @desc
 */
interface IUserRemoteRequest {

    @Throws
    suspend fun checkUser(account: String, password: String): String

    @Throws
    suspend fun getUserInfo(): User
}

