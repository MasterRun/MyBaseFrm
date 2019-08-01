package com.jsongo.mybasefrm

import android.annotation.SuppressLint
import android.app.Application
import android.content.ComponentCallbacks2
import android.content.Context
import com.bumptech.glide.Glide
import com.jsongo.ajs.AJs
import com.jsongo.ajs.webloader.DefaultWebLoader
import com.jsongo.core.BaseCore
import com.jsongo.core.util.ActivityCollector
import com.jsongo.core.view.activity.ScanCodeActivity
import com.jsongo.ui.BaseUI
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.safframework.log.L
import com.vondear.rxfeature.module.scaner.OnRxScanerListener
import org.jetbrains.annotations.Contract

/**
 * @author jsongo
 * @date 2018/9/3 18:40
 */
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        BaseCore.init()
        AJs.init(this)
        BaseUI.init(this)
        BaseCore.isDebug = isDebug

        setScanCodeListener()
    }

    /**
     * 设置扫描信息回调
     */
    fun setScanCodeListener() {
        ScanCodeActivity.setScanerListener(object : OnRxScanerListener {
            override fun onSuccess(type: String?, result: com.google.zxing.Result?) {
                val str = result?.text ?: ""
                val topActivity = ActivityCollector.topActivity
                if (str.startsWith("http://") || str.startsWith("https://")) {
                    DefaultWebLoader.load(str)
                    topActivity.finish()
                } else {
                    QMUIDialog.MessageDialogBuilder(topActivity)
                        .setTitle("扫描结果")
                        .setMessage(str)
                        .addAction("OK", object : QMUIDialogAction.ActionListener {
                            override fun onClick(dialog: QMUIDialog?, index: Int) {
                                topActivity.finish()
                            }
                        })
                        .show()
                }
            }

            override fun onFail(type: String?, message: String?) {
                L.e(message)
            }
        })
    }

    override fun attachBaseContext(context: Context) {
        AppApplication.context = context
        super.attachBaseContext(context)
        BaseCore.attachBaseContext(context)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level == ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN) {
            Glide.get(this).clearMemory()
        }
        Glide.get(this).trimMemory(level)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        //内存低是清理管理的缓存
        Glide.get(this).clearMemory()
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        val isDebug: Boolean
            @Contract(pure = true)
            get() = true
    }
}
