package com.jsongo.core_mini.base_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc activity父类
 */
abstract class BaseActivity : AppCompatActivity(), IPage {

    open val translucentWindow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId)

        initIPage(this)

        if (translucentWindow) {
            //沉浸/透明状态栏
            QMUIStatusBarHelper.translucent(this)
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }
    }

    override fun onDestroyIPage() {
    }

    override fun onDestroy() {
        onDestroyIPage()
        super.onDestroy()
    }
}