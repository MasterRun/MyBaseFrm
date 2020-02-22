package com.jsongo.ajs.webloader

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.util.ConstValue
import com.jsongo.core.common.ActivityCollector

/**
 * 不使用
 */
open class AJsApplet : AJsWebPage() {

    override fun onBackPressed() {
        val onBackPressed = jsWebLoader.onBackPressed()

        if (!onBackPressed) {
            val mainActivityName = getString(com.jsongo.core.R.string.MainActivity)
            try {
                //获取MainActivity的taskid，并将其移到前台
                val taskId = ActivityCollector.getActivities()
                    .first { it.componentName.className == mainActivityName }.taskId
                val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                activityManager.moveTaskToFront(taskId, 0)
            } catch (e: Exception) {
                val intent = Intent(this, Class.forName(mainActivityName))
                startActivity(intent)
            }
            //当前页面移到后台
            moveTaskToBack(true)
        }

    }

    companion object {
        @JvmStatic
        fun load(
            url: String,
            context: Context = AJs.context,
            showTopBar: Boolean = true,
            showProgress: Boolean = false,
            bgColor: String = AJs.context.getString(R.string.ajs_default_bg_color),
            fixHeight: Boolean = true
        ) = context.startActivity(
            Intent(context, AJsApplet::class.java)
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

                .addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        )

    }
}
