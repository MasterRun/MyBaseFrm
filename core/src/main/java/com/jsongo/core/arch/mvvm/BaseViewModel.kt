package com.jsongo.core.arch.mvvm

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * @author  jsongo
 * @date 2019/3/26 10:34
 */
abstract class BaseViewModel : ViewModel() {

    val mainScope: CoroutineScope by lazy {
        MainScope()
    }

    val compositeDisposable: CompositeDisposable by lazy {
        CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()
        mainScope.cancel()
        compositeDisposable.dispose()
    }
}