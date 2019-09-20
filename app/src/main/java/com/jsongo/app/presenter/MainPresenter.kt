package com.jsongo.app.presenter

import com.jsongo.annotation.anno.Model
import com.jsongo.app.model.MainModel
import com.jsongo.app.mvp.IMain
import com.jsongo.core.mvp.base.BasePresenter
import kotlinx.coroutines.launch

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
        getAuthtypes()
    }

    fun getAuthtypes() {
        mainScope.launch {
            try {
                val data = model.getAuthtypes().getAsJsonObject("data").toString()
                view?.onGetDailyGank(data)
                view?.onPageLoaded()
            } catch (e: Exception) {
                view?.onPageError(e.message)
            }
        }
    }

    fun getDaliyDank() {
        mainScope.launch {
            try {
                val category = model.getDailyGank().getAsJsonArray("category")
                view?.onGetDailyGank(category[0].asString)
                view?.onPageLoaded()
            } catch (e: Exception) {
                view?.onPageError()
            }
        }
    }
}