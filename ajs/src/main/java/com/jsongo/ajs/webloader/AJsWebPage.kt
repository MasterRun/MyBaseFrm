package com.jsongo.ajs.webloader

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager.LayoutParams
import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.util.ConstValue
import com.jsongo.core.arch.BaseActivityWrapper
import com.jsongo.core_mini.util.KeyboardUtil
import com.jsongo.ui.component.screenshot_observe.ScreenshotObserveUtil
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

open class AJsWebPage : BaseActivityWrapper() {

    override var mainLayoutId: Int = R.layout.activity_ajs_web_page

    override var containerIndex: Int = 0

    lateinit var jsWebLoader: BaseWebLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //默认禁用侧滑返回
        setSwipeBackEnable(false)

        window.setSoftInputMode(LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //隐藏输入法
        KeyboardUtil.hideSoftInput(this)
        //可取消dialog
        loadingDialog.setCancelable(true)
        loadingDialog.show()

        initWebLoader()
    }

    open fun initWebLoader() {
        //设置加载的url
        var webPath = ""

        if (intent.hasExtra(ConstValue.webpath)) {
            webPath = intent.getStringExtra(ConstValue.webpath)
        }
        if (webPath.isEmpty()) {
            webPath = getString(R.string.ajs_default_url)
        }

        //获取参数
        val showTopBar = intent.getBooleanExtra(ConstValue.showTopBar, true)
        val showProgress = intent.getBooleanExtra(ConstValue.showProgress, true)
        val fixHeight = intent.getBooleanExtra(ConstValue.fixHeight, true)
        val defaultBgColorStr =
            intent.getStringExtra(ConstValue.bgColor) ?: getString(R.string.ajs_default_bg_color)
        var defaultColor: Int
        try {
            defaultColor = Color.parseColor(defaultBgColorStr)
        } catch (e: Exception) {
            e.printStackTrace()
            defaultColor = Color.WHITE
        }

        jsWebLoader = AJsWebLoader.newInstance(
            webPath,
            showTopBar,
            showProgress,
            fixHeight = fixHeight,
            bgColor = defaultColor
        ).apply {
            this.loadingDialog = this@AJsWebPage.loadingDialog
        }

        if (fixHeight && !showTopBar) {
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_webloader_container, jsWebLoader)
        transaction.commit()
    }

    override fun onBackPressed() {
        val onBackPressed = jsWebLoader.onBackPressed()
        if (!onBackPressed) {
            super.onBackPressed()
        }
    }

    override fun onDestroyIPage() {
        ScreenshotObserveUtil.unregister(this)
        super.onDestroyIPage()
    }

    companion object {
        @JvmStatic
        fun load(
            url: String,
            context: Context = AJs.context,
            showTopBar: Boolean = true,
            showProgress: Boolean = true,
            bgColor: String = AJs.context.getString(R.string.ajs_default_bg_color),
            fixHeight: Boolean = true
        ) = context.startActivity(
            Intent(context, AJsWebPage::class.java)
                //加载的路径
                .putExtra(ConstValue.webpath, url)
                //是否显示topbar
                .putExtra(ConstValue.showTopBar, showTopBar)
                //是否显示progress
                .putExtra(ConstValue.showProgress, showProgress)
                //背景色(修复状态栏高度使用的背景色)
                .putExtra(ConstValue.bgColor, bgColor)
                //是否修复状态栏高度(默认是,在隐藏标题栏时,不修复高度,会导致内容顶到状态栏)
                .putExtra(ConstValue.fixHeight, fixHeight)
        )

    }
}
