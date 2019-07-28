package com.jsongo.mybasefrm.mvp

import com.google.gson.JsonObject
import com.jsongo.core.mvp.base.IBaseMvp

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:41
 * desc : mainfragment fragment
 */
interface IMainModule {

    interface IModel : IBaseMvp.IBaseModel {
        suspend fun getDailyGank(): JsonObject
    }

    interface IView : IBaseMvp.IBaseView {
        fun onGetDailyGank(txt: String?)
    }

    interface IPresenter<M : IModel, V : IView> : IBaseMvp.IBasePresenter<M, V>
}