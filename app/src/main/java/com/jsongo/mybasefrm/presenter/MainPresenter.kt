package com.jsongo.mybasefrm.presenter

import com.google.gson.JsonObject
import com.jsongo.core.annotations.Model
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.core.network.ApiCallback
import com.jsongo.mybasefrm.model.MainModel
import com.jsongo.mybasefrm.mvp.IMain

/**
 * author ： jsongo
 * createtime ： 2019/7/23 13:52
 * desc :
 */
class MainPresenter(view: IMain.IView) : BasePresenter<IMain.IModel, IMain.IView>(view),
    IMain.IPresenter<IMain.IModel, IMain.IView> {

    @Model(MainModel::class)
    override lateinit var model: IMain.IModel

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