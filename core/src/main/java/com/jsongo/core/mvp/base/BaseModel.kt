package com.jsongo.core.mvp.base

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * @author  jsongo
 * @date 2019/3/26 17:14
 */
abstract class BaseModel : IBaseMvp.IBaseModel {
    override val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun dispose() {
        compositeDisposable.dispose()
    }

}