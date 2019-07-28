package com.jsongo.mybasefrm.model

import com.google.gson.JsonObject
import com.jsongo.core.mvp.base.BaseModel
import com.jsongo.core.network.ApiCallback
import com.jsongo.core.network.ApiManager
import com.jsongo.mybasefrm.mvp.IMainModule
import com.jsongo.mybasefrm.nework.ApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:50
 * desc :
 */
class MainModuleModel : BaseModel(), IMainModule.IModel {
    override fun getDailyGank(callback: ApiCallback<JsonObject>) {
        val disposable = ApiManager.createApiService(ApiService::class.java)
            .dailyGank()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it.getAsJsonPrimitive("error").asBoolean) {
                    callback.onFailed(0, "", it)
                } else {
                    callback.onSuccess(it)
                }
            }, {
                callback.onFailed(0, it.message ?: "", it)
            })
        addDisposable(disposable)
    }
}