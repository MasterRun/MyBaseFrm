package com.jsongo.core.ui.splash

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.jsongo.core.R
import com.jsongo.core.base.BaseActivity
import com.jsongo.core.util.ActivityCollector
import com.tbruyelle.rxpermissions2.RxPermissions
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

        validatePermission(this)
    }

    /**
     * 权限
     */
    open fun getPermissions() = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO
    )

    protected open fun validatePermission(fragmentActivity: FragmentActivity) {
        val permissions = RxPermissions(fragmentActivity)
        //noinspection ResultOfMethodCallIgnored
        val disposable = permissions.request(*getPermissions()).subscribe { granted ->
            if (!granted) {
                if (permissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE).not()) {
                    //如果没有写权限，提示，延迟2秒退出
                    RxToast.warning(getString(R.string.nopermission_exit))
                    exitDelay()
                } else {
                    //提示权限丢失，启动首页
                    RxToast.warning(getString(R.string.permission_missing))
                    startMainActivity(0)
                }
            } else {
                startMainActivity(0)
            }
        }
        compositeDisposable.add(disposable)
    }

    /**
     * 启动MainActivity
     */
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
