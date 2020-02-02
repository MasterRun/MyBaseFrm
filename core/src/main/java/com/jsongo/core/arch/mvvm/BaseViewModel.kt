package com.jsongo.core.arch.mvvm

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author  jsongo
 * @date 2019/3/26 10:34
 */
abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    val mainScope: CoroutineScope by lazy {
        MainScope()
    }

    val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    fun addDisposable(disposable: Disposable?) {
        disposable?.let {
            compositeDisposable.add(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        mainScope.cancel()
        compositeDisposable.dispose()
    }
}