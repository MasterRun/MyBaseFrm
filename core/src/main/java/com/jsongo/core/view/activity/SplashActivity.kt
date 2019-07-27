package com.jsongo.core.view.activity

import android.content.Intent
import android.os.Bundle
import com.jsongo.core.R
import com.jsongo.core.mvp.base.BaseActivity
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity() {

    override var mainLayoutId = R.layout.activity_splash
    override var containerIndex = 3


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        flMainContainer3.setPadding(0, 0, 0, 0)
        val disposable = Observable.timer(800, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val mainActivityName = getString(R.string.MainActivity)
                if (mainActivityName.equals(this@SplashActivity::class.java.name)) {
                    finish()
                } else {
                    val intent = Intent(this@SplashActivity, Class.forName(mainActivityName))
                    startActivity(intent)
                }
            }
    }
}
