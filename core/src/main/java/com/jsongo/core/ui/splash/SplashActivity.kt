package com.jsongo.core.ui.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import com.jsongo.annotation.anno.permission.PermissionDeny
import com.jsongo.annotation.anno.permission.PermissionNeed
import com.jsongo.core.R
import com.jsongo.core.arch.BaseActivity
import com.jsongo.core.util.ActivityCollector
import com.vondear.rxtool.view.RxToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * 启动页
 */
open class SplashActivity : BaseActivity() {

    override var mainLayoutId = R.layout.activity_splash
    override var containerIndex = 0

    protected val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startMainActivity(0)
    }

    /**
     * 启动MainActivity
     */
    @PermissionNeed(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    open fun startMainActivity(delay: Long = 800) {
        val disposable = Observable.timer(delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                val mainActivityName = getString(R.string.MainActivity)
                //如果MainActivity也是启动页，直接finish
                if (mainActivityName.equals(this@SplashActivity::class.java.name)) {
                    //其实配置首页
                    RxToast.error(getString(R.string.please_config_mainpage))
                    exitDelay()
                } else {
                    //start MainActivity
                    val intent = Intent(this@SplashActivity, Class.forName(mainActivityName))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }, {
                RxToast.error(getString(R.string.start_mainpage_error) + it.message)
            })
        compositeDisposable.add(disposable)
    }

    @PermissionDeny
    fun permissionDeny() {
        RxToast.warning(getString(R.string.nopermission_exit))
        exitDelay()
    }

    /**
     * 延迟退出app
     */
    fun exitDelay(delay: Long = 2000) {
        val disposable = Observable.timer(delay, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                ActivityCollector.appExit()
            }
        compositeDisposable.add(disposable)
    }

    override fun onIPageDestroy() {
        compositeDisposable.dispose()
        super.onIPageDestroy()
    }
}
