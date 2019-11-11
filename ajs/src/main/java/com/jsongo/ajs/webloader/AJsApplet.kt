package com.jsongo.ajs.webloader

import android.content.Intent
import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.util.ConstValue

/**
 * 不使用
 */
open class AJsApplet : AJsWebPage() {

    override fun onBackPressed() {

        val onBackPressed = jsWebLoader.onBackPressed()

        if (!onBackPressed) {
            val mainActivityName = getString(com.jsongo.core.R.string.MainActivity)
            val intent = Intent(this, Class.forName(mainActivityName))
//            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
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
            val intent = Intent(AJs.context, AJsApplet::class.java)
            //加载的路径
            intent.putExtra(ConstValue.webpath, url)
            //是否显示topbar
            intent.putExtra(ConstValue.showTopBar, showTopBar)
            //背景色(修复状态栏高度使用的背景色)
            intent.putExtra(ConstValue.bgColor, bgColor)
            //是否修复状态栏高度(默认是,在隐藏标题栏时,不修复高度,会导致内容顶到状态栏)
            intent.putExtra(ConstValue.fixHeight, fixHeight)

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            AJs.context.startActivity(intent)
        }
    }
}
