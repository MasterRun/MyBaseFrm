package com.jsongo.mybasefrm

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.*

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc activity父类
 */
abstract class BaseActivity : AppCompatActivity() {

    private var slidingLayout: SlidingLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)

        slidingLayout = SlidingLayout(this)
        setSwipeBackEnable(true)

    }

    fun setSwipeBackEnable(isEnable: Boolean) {
        if (isEnable) {
            this.slidingLayout?.bindActivity(this)
        } else {
            this.slidingLayout?.setEnableSlidClose(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
        ActivityCollector.removeActivity(this)
    }
}