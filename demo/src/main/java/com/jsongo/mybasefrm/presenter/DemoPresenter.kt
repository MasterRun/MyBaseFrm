package com.jsongo.mybasefrm.presenter

import com.jsongo.annotation.anno.Model
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.mybasefrm.model.DemoModel
import com.jsongo.mybasefrm.mvp.IDemo
import kotlinx.coroutines.launch

/**
 * author ： jsongo
 * createtime ： 2019/7/23 13:52
 * desc :
 */
class DemoPresenter(view: IDemo.IView) : BasePresenter<IDemo.IModel, IDemo.IView>(view),
    IDemo.IPresenter<IDemo.IModel, IDemo.IView> {

    @Model(DemoModel::class)
    override lateinit var model: IDemo.IModel

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