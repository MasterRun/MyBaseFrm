package com.jsongo.mybasefrm.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jsongo.mybasefrm.BaseActivity
import com.jsongo.mybasefrm.R
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

class MainActivity : BaseActivity() {

    override fun setLayout() = R.layout.activity_main

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

    }

}
