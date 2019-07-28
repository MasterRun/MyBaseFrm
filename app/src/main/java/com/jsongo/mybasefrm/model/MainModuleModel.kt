package com.jsongo.mybasefrm.model

import com.google.gson.JsonObject
import com.jsongo.core.mvp.base.BaseModel
import com.jsongo.core.network.ApiManager
import com.jsongo.mybasefrm.mvp.IMainModule
import com.jsongo.mybasefrm.nework.ApiService

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:50
 * desc :
 */
class MainModuleModel : BaseModel(), IMainModule.IModel {
    override suspend fun getDailyGank(): JsonObject {
        return ApiManager.createApiService(ApiService::class.java).dailyGank2()
    }
}