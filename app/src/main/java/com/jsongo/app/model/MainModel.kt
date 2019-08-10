package com.jsongo.app.model

import com.google.gson.JsonObject
import com.jsongo.app.mvp.IMain
import com.jsongo.core.mvp.base.BaseModel
import com.jsongo.core.network.ApiManager
import com.jsongo.mybasefrm.nework.ApiService

/**
 * author ： jsongo
 * createtime ： 2019/7/23 13:46
 * desc :
 */
class MainModel : BaseModel(), IMain.IModel {

    override suspend fun getDailyGank(): JsonObject {
        return ApiManager.createApiService(ApiService::class.java).dailyGank()
    }

    override suspend fun getAuthtypes(): JsonObject {
        return ApiManager.createApiService(ApiService::class.java).authtypes()
    }

}