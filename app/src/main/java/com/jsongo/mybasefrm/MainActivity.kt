package com.jsongo.mybasefrm

import android.content.Intent
import android.os.Bundle
import com.jsongo.mybasefrm.jsloader.DefaultWebLoader
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.vondear.rxfeature.activity.ActivityScanerCode
import com.vondear.rxfeature.module.scaner.OnRxScanerListener
import com.vondear.rxtool.RxActivityTool
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSwipeBackEnable(false)
        QMUIStatusBarHelper.translucent(this)
        QMUIStatusBarHelper.setStatusBarLightMode(this)
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

                }
            })
            val intent = Intent(this@MainActivity, ActivityScanerCode::class.java)
            startActivity(intent)
        }
    }

}
