package com.jsongo.mybasefrm

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jsongo.mybasefrm.jsloader.DefaultWebLoader
import com.vondear.rxfeature.activity.ActivityScanerCode
import com.vondear.rxfeature.module.scaner.OnRxScanerListener
import com.vondear.rxtool.RxActivityTool
import kotlinx.android.synthetic.main.activity_base.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun setLayout() = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSwipeBackEnable(false)

        topbar.backImageButton.visibility = View.GONE

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

        btn_loadbaidu.setOnClickListener {
            DefaultWebLoader.load("https://www.baidu.com")
        }
    }

}
