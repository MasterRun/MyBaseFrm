package com.jsongo.ajs.webloader

import android.app.ActivityManager
import android.app.ActivityManager.TaskDescription
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.util.ConstValue
import com.jsongo.core_mini.common.ActivityCollector
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import kotlin.random.Random

/**
 * 模拟小程序
 */
open class AJsApplet : AJsWebPage() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTaskDescription(TaskDescription("applet" + Random.nextInt(10), null))
    }

    override fun initWebLoader() {
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

        jsWebLoader = AJsAppletLoader.newInstance(
            webPath,
            showTopBar,
            showProgress,
            fixHeight = fixHeight,
            bgColor = defaultColor
        ).apply {
            this.loadingDialog = this@AJsApplet.loadingDialog
        }

        if (fixHeight && !showTopBar) {
            QMUIStatusBarHelper.setStatusBarLightMode(this)
        }

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_webloader_container, jsWebLoader)
        transaction.commit()
    }

    override fun finish() {
        val mainActivityName = getString(com.jsongo.core.R.string.MainActivity)
        try {
            //获取MainActivity的taskid，并将其移到前台
            val taskId = ActivityCollector.activities
                .first { it.componentName.className == mainActivityName }.taskId
            val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityManager.moveTaskToFront(taskId, 0)
        } catch (e: Exception) {
            /*startActivity(
                Intent(
                    this,
                    Class.forName(mainActivityName)
                ).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )*/
        }
        //当前页面移到后台
        moveTaskToBack(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        finishAndRemoveTask()
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
