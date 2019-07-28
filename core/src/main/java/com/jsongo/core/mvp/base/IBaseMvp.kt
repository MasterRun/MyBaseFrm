package com.jsongo.core.mvp.base

import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import java.lang.ref.WeakReference

/**
 * @author  jsongo
 * @date 2019/3/26 10:29
 */
interface IBaseMvp {

    interface IBaseModel {
        val compositeDisposable: CompositeDisposable
        fun dispose()
    }

    interface IBaseView : PageStatusListener {
        fun initView()
        fun initPresenter() {}
    }

    interface IBasePresenter<out M : IBaseModel, out V : IBaseView> {
        val model: M
        val weakView: WeakReference<out V>
        val mainScope: CoroutineScope
        val compositeDisposable: CompositeDisposable

        fun start()
        fun onDestory()
    }
}