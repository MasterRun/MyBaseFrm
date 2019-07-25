package com.jsongo.mybasefrm.presenter

import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.mybasefrm.model.MainModel
import com.jsongo.mybasefrm.mvp.IMain
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * author ： jsongo
 * createtime ： 2019/7/23 13:52
 * desc :
 */
class MainPresenter(view: IMain.IView) : BasePresenter<IMain.IModel, IMain.IView>(view),
    IMain.IPresenter<IMain.IModel, IMain.IView> {
    override val model: IMain.IModel

    init {

        model = MainModel()
    }

    override fun start() {

        val disposable = Observable.timer(1200, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view?.onPageLoaded()
            }
        addDisposable(disposable)
    }
}