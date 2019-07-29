package com.jsongo.core.mvp.base

import android.Manifest
import com.jsongo.core.annotations.ModelBinder
import com.jsongo.core.util.ActivityCollector
import com.tbruyelle.rxpermissions2.RxPermissions
import com.vondear.rxtool.view.RxToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

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
        ModelBinder.bind(this)
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }


    fun validatePermission(baseMvpActivity: BaseMvpActivity<IBaseMvp.IBaseModel, IBaseMvp.IBaseView>) {
        val permissions = RxPermissions(baseMvpActivity)
        //noinspection ResultOfMethodCallIgnored
        val disposable = permissions.request(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        ).subscribe { granted ->
            if (!granted) {
                if (permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE).not()) {
                    RxToast.warning("程序即将退出!")
                    val disposable = Observable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            ActivityCollector.appExit()
                        }
                    addDisposable(disposable)
                }
            }
        }
        addDisposable(disposable)
    }

    companion object {

    }

    override fun onDestory() {
        model.dispose()
        weakView.clear()
        mainScope.cancel()
        compositeDisposable.dispose()
    }
}