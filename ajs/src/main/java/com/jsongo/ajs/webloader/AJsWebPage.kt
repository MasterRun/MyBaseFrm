package com.jsongo.ajs.webloader

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.helper.ConstValue
import com.jsongo.core.mvp.base.BaseActivity
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.vondear.rxtool.RxKeyboardTool

open class AJsWebPage : BaseActivity() {

    override var mainLayoutId: Int = R.layout.activity_ajs_web_page

    override var containerIndex: Int = 0

    lateinit var jsWebLoader: BaseWebLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //默认禁用侧滑返回
        setSwipeBackEnable(false)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        //隐藏输入法
        RxKeyboardTool.hideSoftInput(this)
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
            webPath = getString(R.string.ajs_default_url);
        }

        //获取参数
        val showTopBar = intent.getBooleanExtra(ConstValue.showTopBar, true)
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

        jsWebLoader = AJsWebLoader.newInstance(webPath, showTopBar, fixHeight = fixHeight)
        (jsWebLoader as AJsWebLoader).loadingDialog = loadingDialog

        if (fixHeight && !showTopBar) {
            QMUIStatusBarHelper.setStatusBarLightMode(this)
            jsWebLoader.rlLayoutRoot.apply {
                setBackgroundColor(defaultColor)
            }
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

    companion object {
        @JvmStatic
        fun load(
            url: String,
            showTopBar: Boolean = true,
            bgColor: String = AJs.context.getString(R.string.ajs_default_bg_color),
            fixHeight: Boolean = true
        ) {
            val intent = Intent(AJs.context, AJsWebPage::class.java)
            //加载的路径
            intent.putExtra(ConstValue.webpath, url)
            //是否显示topbar
            intent.putExtra(ConstValue.showTopBar, showTopBar)
            //背景色(修复状态栏高度使用的背景色)
            intent.putExtra(ConstValue.bgColor, bgColor)
            //是否修复状态栏高度(默认是,在隐藏标题栏时,不修复高度,会导致内容顶到状态栏)
            intent.putExtra(ConstValue.fixHeight, fixHeight)

            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            AJs.context.startActivity(intent)
        }
    }
}
