package com.jsongo.mybasefrm.mvp

import com.google.gson.JsonObject
import com.jsongo.core.mvp.base.IBaseMvp

/**
 * author ： jsongo
 * createtime ： 2019/7/23 13:46
 * desc :
 */
interface IMain {
    interface IModel : IBaseMvp.IBaseModel {
        suspend fun getDailyGank(): JsonObject
        suspend fun getAuthtypes(): JsonObject
    }

    interface IView : IBaseMvp.IBaseView {
        fun onGetDailyGank(txt: String?)
    }

    interface IPresenter<M : IModel, V : IView> : IBaseMvp.IBasePresenter<M, V>
}