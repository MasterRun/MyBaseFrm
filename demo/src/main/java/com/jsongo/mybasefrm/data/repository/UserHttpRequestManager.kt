package com.jsongo.mybasefrm.data.repository

import com.jsongo.core.network.ApiManager
import com.jsongo.core.network.checkResult
import com.jsongo.mybasefrm.bean.User
import com.jsongo.mybasefrm.data.api.UserApiService

/**
 * @author ： jsongo
 * @date ： 2019/11/9 23:26
 * @desc :
 */

object UserHttpRequestManager : IUserRemoteRequest {

    @Throws
    override suspend fun checkUser(account: String, password: String): String = checkResult {
        ApiManager.createApiServiceWithoutAuth(UserApiService::class.java)
            .checkUser(account, password)
    }

    @Throws
    override suspend fun getUserInfo(): User = checkResult {
        ApiManager.createApiService(UserApiService::class.java).getUserInfo()
    }
}
