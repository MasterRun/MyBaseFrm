package com.jsongo.core.view.activity

import android.content.Intent
import android.os.Bundle
import com.jsongo.core.R
import com.jsongo.core.mvp.base.BaseActivity

class SplashActivity : BaseActivity() {

    override var mainLayoutId = R.layout.activity_splash
    override var containerIndex = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

/*        val disposable = Observable.timer(800, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
            }*/
        rlLayoutRoot.setOnClickListener {
            val mainActivityName = getString(R.string.MainActivity)
            if (mainActivityName.equals(this@SplashActivity::class.java.name)) {
                finish()
            } else {
                val intent = Intent(this@SplashActivity, Class.forName(mainActivityName))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }
}
