package com.jsongo.core.crash

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.core.util.ConstConf
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.safframework.log.L
import com.vondear.rxtool.RxDeviceTool
import com.vondear.rxtool.RxTimeTool
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileWriter

class CrashActivity : BaseActivity() {

    override var mainLayoutId = R.layout.activity_crash
    override var containerIndex = 3

    /**
     * 发生时间
     */
    private lateinit var occurTime: String
    private val compositeDisposable = CompositeDisposable()

    /**
     * 崩溃信息头
     */
    private val crashHeader: String
        get() {
            return "\n************* Crash Log Head ****************" +
                    "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                    "\nDevice Model       : " + Build.MODEL +// 设备型号
                    "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                    "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                    "\nApp VersionName    : " + RxDeviceTool.getAppVersionName(BaseCore.context) +
                    "\nApp VersionCode    : " + RxDeviceTool.getAppVersionNo(BaseCore.context) +
                    "\nTime               : " + occurTime +
                    "\n************* Crash Log Head ****************\n\n"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSwipeBackEnable(false)

        clLayoutContainer.setBackgroundColor(Color.TRANSPARENT)

        occurTime = RxTimeTool.getCurTimeString()
        var crashLog = crashHeader
        if (intent.hasExtra("crash_log")) {
            crashLog = crashHeader + intent.getStringExtra("crash_log")
        }

        saveCrashLog(crashLog)

        showCrashDialog(crashLog)
    }

    /**
     * 保存日志
     */
    private fun saveCrashLog(crashLog: String) {

        val disposable = Observable.just(ConstConf.CRASH_LOG_DIR)
            .map {
                val file = File(it, "${occurTime.replace(" ", "_")}.log")
                if (file.parentFile.exists().not()) {
                    if (!file.parentFile.mkdirs()) {
                        throw  Exception()
                    }
                }
                if (!file.createNewFile()) {
                    throw  Exception()
                }
                file
            }
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe({
                val fileWriter = FileWriter(it)
                fileWriter.write(crashLog)
                fileWriter.flush()
                fileWriter.close()
            }, {
                it.printStackTrace()
                L.e(it.message)
            })
        compositeDisposable.add(disposable)
    }

    /**
     * 显示dialog
     */
    private fun showCrashDialog(crashLog: String?) {
        val dialogBuilder = QMUIDialog.MessageDialogBuilder(this)
            .setTitle("出错啦！")
            .setMessage(crashLog)
            .setCanceledOnTouchOutside(false)
            .addAction("请联系管理员反馈") { dialog, index ->
                dialog?.dismiss()
                finish()
            }
            .addAction(
                0,
                "取消",
                QMUIDialogAction.ACTION_PROP_NEGATIVE
            ) { dialog, index ->
                dialog?.dismiss()
                finish()
            }
        dialogBuilder.show()
        dialogBuilder.textView?.textSize = 12f
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
