package com.jsongo.mybasefrm.data.repository

import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.bean.toErrorDataWrapper
import com.jsongo.core.network.ApiManager
import com.jsongo.mybasefrm.bean.User
import com.jsongo.mybasefrm.data.api.UserApiService

/**
 * @author ： jsongo
 * @date ： 2019/11/9 23:26
 * @desc :
 */

object UserHttpRequestManager : IUserRemoteRequest {

    @Throws
    override suspend fun checkUser(account: String, password: String): String {
        val checkUserWrapper: DataWrapper<String?>
        try {
            checkUserWrapper =
                ApiManager.createApiServiceWithoutAuth(UserApiService::class.java).checkUser(account, password)
        } catch (e: Exception) {
            throw NetFailedException(e.message.toErrorDataWrapper())
        }
        if (checkUserWrapper.code > 0 && !checkUserWrapper.data.isNullOrEmpty()) {
            return checkUserWrapper.data!!
        } else {
            throw NetFailedException(checkUserWrapper.toErrorDataWrapper())
        }
    }

    @Throws
    override suspend fun getUserInfo(): User {
        val userWrapper: DataWrapper<User?>
        try {
            userWrapper =
                ApiManager.createApiService(UserApiService::class.java).getUserInfo()
        } catch (e: Exception) {
            throw NetFailedException(e.message.toErrorDataWrapper())
        }
        if (userWrapper.code > 0 && userWrapper.data != null) {
            return userWrapper.data!!
        } else {
            throw NetFailedException(userWrapper.toErrorDataWrapper())
        }
    }
}
