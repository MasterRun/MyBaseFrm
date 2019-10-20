package com.jsongo.app.view.activity

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.core.util.ActivityCollector
import com.tbruyelle.rxpermissions2.RxPermissions
import com.vondear.rxtool.view.RxToast
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * @author ： jsongo
 * @date ： 19-10-20 下午6:09
 * @desc :
 */
class SplashActivity : BaseActivity() {

    override var mainLayoutId = R.layout.activity_splash
    override var containerIndex = 0

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        validatePermission(this)
    }

    private fun validatePermission(fragmentActivity: FragmentActivity) {
        val permissions = RxPermissions(fragmentActivity)
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
                    RxToast.warning(BaseCore.context.getString(R.string.nopermission_exit))
                    val disposable = Observable.timer(2, TimeUnit.SECONDS)
                        .subscribeOn(Schedulers.computation())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            ActivityCollector.appExit()
                        }
                    compositeDisposable.add(disposable)
                } else {
                    startMainActivity()
                }
            } else {
                startMainActivity()
            }
        }
        compositeDisposable.add(disposable)
    }

    private fun startMainActivity() {
        val disposable = Observable.timer(800, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val mainActivityName = getString(R.string.MainActivity)
                if (mainActivityName.equals(this@SplashActivity::class.java.name)) {
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity, Class.forName(mainActivityName))
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }
        compositeDisposable.add(disposable)
    }

    override fun onIPageDestroy() {
        compositeDisposable.dispose()
        super.onIPageDestroy()
    }
}
