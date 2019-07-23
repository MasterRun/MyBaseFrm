package com.jsongo.mybasefrm.mvp

import com.jsongo.core.mvp.base.IBaseMvp

/**
 * author ： jsongo
 * createtime ： 2019/7/23 13:46
 * desc :
 */
interface IMain {
    interface IModel : IBaseMvp.IBaseModel

    interface IView : IBaseMvp.IBaseView

    interface IPresenter<M : IModel, V : IView> : IBaseMvp.IBasePresenter<M, V>
}