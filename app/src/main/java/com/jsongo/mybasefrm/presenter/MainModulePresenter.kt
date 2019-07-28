package com.jsongo.mybasefrm.presenter

import com.google.gson.JsonObject
import com.jsongo.core.annotations.Model
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.core.network.ApiCallback
import com.jsongo.mybasefrm.model.MainModuleModel
import com.jsongo.mybasefrm.mvp.IMainModule

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:50
 * desc :
 */
class MainModulePresenter(view: IMainModule.IView) :
    BasePresenter<IMainModule.IModel, IMainModule.IView>(view),
    IMainModule.IPresenter<IMainModule.IModel, IMainModule.IView> {

    @Model(MainModuleModel::class)
    override lateinit var model: IMainModule.IModel

    override fun start() {
        model.getDailyGank(object : ApiCallback<JsonObject> {
            override fun onSuccess(t: JsonObject) {
                val category = t.getAsJsonArray("category")
                view?.onGetDailyGank(category[0].asString)
                view?.onPageLoaded()
            }

            override fun onFailed(code: Int, msg: String, obj: Any?) {
                view?.onPageError()
            }
        })
    }
}