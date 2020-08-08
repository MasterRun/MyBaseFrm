package com.jsongo.core_mini.base_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jsongo.core_mini.widget.ILoadingDialog
import com.jsongo.core_mini.widget.IStatusView
import com.jsongo.core_mini.widget.ITopbar
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc activity父类
 */
abstract class BaseActivity : AppCompatActivity(), IPage {

    override var topbar: ITopbar? = null
    override var loadingDialog: ILoadingDialog? = null
    override var statusView: IStatusView? = null

    open val translucentWindow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(layoutId)

        if (translucentWindow) {
            //沉浸/透明状态栏
            QMUIStatusBarHelper.translucent(this)
            QMUIStatusBarHelper.setStatusBarDarkMode(this)
        }

        initIPage(this)
    }

    override fun onDestroyIPage() {
    }

    override fun onDestroy() {
        onDestroyIPage()
        super.onDestroy()
    }
}