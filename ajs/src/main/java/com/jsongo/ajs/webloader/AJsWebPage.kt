package com.jsongo.ajs.webloader

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.view.WindowManager
import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.helper.ConstValue
import com.jsongo.core.mvp.base.BaseActivity
import com.vondear.rxtool.RxKeyboardTool
import kotlinx.android.synthetic.main.activity_ajs_web_page.*

class AJsWebPage : BaseActivity() {

    override var mainLayoutId: Int = R.layout.activity_ajs_web_page

    override var containerIndex: Int = 0

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


        //设置加载的url
        var webPath = ""

        if (intent.hasExtra(ConstValue.webpath)) {
            webPath = intent.getStringExtra(ConstValue.webpath)
        }
        if (webPath.isEmpty()) {
            webPath = "https://www.baidu.com";
        }
        val fragWebLoader = frag_webloader
        if (fragWebLoader is AJsWebLoader) {
            fragWebLoader.webPath = webPath
            fragWebLoader.loadingDialog = loadingDialog
        }

    }

    companion object {
        fun load(url: String) {
            val intent = Intent(AJs.context, AJsWebPage::class.java)
            intent.putExtra(ConstValue.webpath, url)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            AJs.context.startActivity(intent)
        }
    }
}
