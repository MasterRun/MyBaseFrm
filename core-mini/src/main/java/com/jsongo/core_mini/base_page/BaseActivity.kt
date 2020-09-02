package com.jsongo.core_mini.base_page

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatActivity
import com.jsongo.core_mini.common.ActivityCollector
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc activity父类
 */
@Keep
abstract class BaseActivity : AppCompatActivity(), IPage {

    open val translucentWindow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (translucentWindow) {
            //沉浸/透明状态栏
            QMUIStatusBarHelper.translucent(this)
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }

        initIPage(this)
    }

    override fun initIPage(context: Context) {
        setContentView(layoutId)
    }

    override fun onDestroyIPage() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ActivityCollector.markActivityFore(this, true)
    }

    override fun onDestroy() {
        onDestroyIPage()
        super.onDestroy()
    }
}