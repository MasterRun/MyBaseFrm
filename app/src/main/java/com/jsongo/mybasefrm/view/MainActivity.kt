package com.jsongo.mybasefrm.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jsongo.mybasefrm.BaseActivity
import com.jsongo.mybasefrm.PrintLog
import com.jsongo.mybasefrm.jsloader.DefaultWebLoader
import com.jsongo.mybasefrm.util.SmartRefreshHeader
import com.jsongo.mybasefrm.util.initWithStr
import com.jsongo.mybasefrm.util.useHeader
import com.safframework.log.L
import com.vondear.rxfeature.activity.ActivityScanerCode
import com.vondear.rxfeature.module.scaner.OnRxScanerListener
import com.vondear.rxtool.RxActivityTool
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File


class MainActivity : BaseActivity() {

//    val topbbb:TopbarLayout by findViewById<TopbarLayout>(R.id.topbar)

    override var mainLayoutId = com.jsongo.mybasefrm.R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSwipeBackEnable(false)

        topbar.backImageButton.visibility = View.GONE

        smart_refresh_layout
            .useHeader(this, SmartRefreshHeader.StoreHouseHeader)
//            .useFooter(this, SmartRefreshFooter.BallPulseFooter)
            .initWithStr("loading...")

            .setOnRefreshListener {
                RxToast.success("refresh")
                it.finishRefresh(1000)
            }
            .setOnLoadMoreListener {
                RxToast.success("loadmore")
                it.finishLoadMore(1000)
            }

        btn.setOnClickListener {
            DefaultWebLoader.load("")
        }
        btn_scan.setOnClickListener {
            ActivityScanerCode.setScanerListener(object : OnRxScanerListener {
                override fun onSuccess(type: String?, result: com.google.zxing.Result?) {
                    DefaultWebLoader.load(result?.text ?: "")
                    RxActivityTool.finishActivity()
                    RxActivityTool.currentActivity().finish()
                }

                override fun onFail(type: String?, message: String?) {
                    L.e(message)
                }
            })
            val intent = Intent(this@MainActivity, ActivityScanerCode::class.java)
            startActivity(intent)
        }

        btn_loadbaidu.setOnClickListener {
            DefaultWebLoader.load("https://www.baidu.com")
        }

        btn_gomap.setOnClickListener {
            val intent = Intent(this@MainActivity, ArcgisDemoActivity::class.java)
            startActivity(intent)
        }

        btn_test.setOnClickListener {

            //goEpsonPrintPreview("/sdcard/Tencent/QQfile_recv/ttt.pdf")
            //图片打开失败！
//            goEpsonPrintPreview2("/sdcard/Tencent/QQfile_recv/ttt.png")
        }

    }

    fun goEpsonPrintPreview(path: String) {
        val epsonPrintApkPackageName = "epson.print"
        val intent = Intent()
        intent.action = "android.intent.action.SEND"
        intent.setPackage(epsonPrintApkPackageName)
        intent.setClassName(epsonPrintApkPackageName, "epson.print.ActivityDocsPrintPreview")
        val uriForFile = android.support.v4.content.FileProvider.getUriForFile(
            this,
            "com.jsongo.mybasefrm.fileprovider",
            File(path)
        )
//        val uri = Uri.fromFile(File(path))
        intent.putExtra("android.intent.extra.STREAM", uriForFile)
        intent.type = "application/pdf"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        startActivity(intent)
    }

    fun goEpsonPrintPreview2(path: String) {
        val epsonPrintApkPackageName = "epson.print"
        val intent = Intent()
        intent.setPackage(epsonPrintApkPackageName)
        intent.setClassName(epsonPrintApkPackageName, "epson.print.ActivityDocsPrintPreview")
        intent.putExtra("from", 3)
        intent.putExtra("FROM_EPSON", true)
        intent.action = "android.intent.action.SEND"


        val uriForFile = android.support.v4.content.FileProvider.getUriForFile(
            this,
            "${packageName}.fileprovider",
            File(path)
        )
//        val uri = Uri.fromFile(File(path))
        intent.putExtra("android.intent.extra.STREAM", uriForFile)
        intent.type = "image/png"
        intent.putExtra("typeprint", true)
        intent.putExtra("print_log", getPrintLog(File(path)))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        startActivity(intent)
    }

    private fun getPrintLog(paramFile: File): PrintLog {
        val localPrintLog = PrintLog()
        localPrintLog.uiRoute = 2
        localPrintLog.originalFileExtension = PrintLog.getFileExtension(paramFile)
        return localPrintLog
    }

}
