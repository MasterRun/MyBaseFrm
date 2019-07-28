package com.jsongo.mybasefrm.presenter

import com.jsongo.core.annotations.Model
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.mybasefrm.model.MainModuleModel
import com.jsongo.mybasefrm.mvp.IMainModule
import kotlinx.coroutines.launch

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