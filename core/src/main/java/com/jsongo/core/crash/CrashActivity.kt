package com.jsongo.core.crash

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.jsongo.core.BaseCore
import com.jsongo.core.R
import com.jsongo.core.arch.BaseActivity
import com.jsongo.core.crash.CrashHandler.Companion.CRASH_LOG
import com.jsongo.core.util.ConstConf
import com.jsongo.core.util.DateUtil
import com.jsongo.core.util.DeviceUtil
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.safframework.log.L
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
            return "\n*********** Crash Log Head ***********" +
                    "\nDevice Manufacturer: " + Build.MANUFACTURER +// 设备厂商
                    "\nDevice Model       : " + Build.MODEL +// 设备型号
                    "\nAndroid Version    : " + Build.VERSION.RELEASE +// 系统版本
                    "\nAndroid SDK        : " + Build.VERSION.SDK_INT +// SDK版本
                    "\nApp VersionName    : " + DeviceUtil.getAppVersionName(BaseCore.context) +
                    "\nApp VersionCode    : " + DeviceUtil.getAppVersionNo(BaseCore.context) +
                    "\nTime               : " + occurTime +
                    "\n*********** Crash Log Head ***********\n\n"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSwipeBackEnable(false)

        rlLayoutRoot.setBackgroundColor(Color.TRANSPARENT)

        occurTime = DateUtil.getCurrentTimeStr()
        var crashLog = crashHeader
        if (intent.hasExtra("crash_log")) {
            crashLog = crashHeader + intent.getStringExtra(CRASH_LOG)
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
            .setTitle(getString(R.string.crash_title))
            .setMessage(crashLog)
            .setCanceledOnTouchOutside(false)
            .addAction(getString(R.string.crash_btn1)) { dialog, index ->
                dialog?.dismiss()
                finish()
            }
            .addAction(
                0, getString(R.string.crash_btn2), QMUIDialogAction.ACTION_PROP_NEGATIVE
            ) { dialog, index ->
                dialog?.dismiss()
                finish()
            }
        val dialog = dialogBuilder.show()

        //获取到TextView，点击复制内容到剪切板
        val contentView =
            dialog.findViewById<ViewGroup>(com.qmuiteam.qmui.R.id.qmui_dialog_content_id)
        val textView = contentView?.getChildAt(0) as TextView
        textView.isEnabled = true
        textView.isClickable = true
        textView.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.primaryClip = ClipData.newPlainText("crashLog", crashLog)
            Toast.makeText(this@CrashActivity, R.string.copy_to_clipboard, Toast.LENGTH_SHORT)
                .show()
        }
        textView.textSize = 12f
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
