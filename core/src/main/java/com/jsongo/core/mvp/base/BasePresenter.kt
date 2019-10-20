package com.jsongo.core.mvp.base

import com.jsongo.annotation.register.PresenterConfigor
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.ref.WeakReference

/**
 * @author  jsongo
 * @date 2019/3/26 10:34
 */
abstract class BasePresenter<out M : IBaseMvp.IBaseModel, out V : IBaseMvp.IBaseView>(view: V) :
    IBaseMvp.IBasePresenter<M, V> {

    override val mainScope: CoroutineScope by lazy {
        MainScope()
    }
    override val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    final override val weakView: WeakReference<out V>

    /**
     * 使用get，只是生成get方法，不能直接赋值不使用get，否则会持有强引用
     */
    val view: V?
        get() = weakView.get()

    init {
        weakView = WeakReference(view)
        PresenterConfigor.config(this)
    }

    override fun onDestory() {
        model.dispose()
        weakView.clear()
        mainScope.cancel()
        compositeDisposable.dispose()
    }
}