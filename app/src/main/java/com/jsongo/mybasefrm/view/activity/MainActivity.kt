package com.jsongo.mybasefrm.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jsongo.ajs.webloader.DefaultWebLoader
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.core.util.SmartRefreshHeader
import com.jsongo.core.util.initWithStr
import com.jsongo.core.util.useHeader
import com.jsongo.mybasefrm.R
import com.safframework.log.L
import com.vondear.rxfeature.activity.ActivityScanerCode
import com.vondear.rxfeature.module.scaner.OnRxScanerListener
import com.vondear.rxtool.RxActivityTool
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

    override var mainLayoutId = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSwipeBackEnable(false)

        topbar.backImageButton.visibility = View.GONE

        smartRefreshLayout
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
            val webPath = "file:///android_asset/web/index.html"

            DefaultWebLoader.load(webPath)
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

        var times = 0

        btn_testdb.setOnClickListener {
            if (times % 2 == 0) {
                val value = CommonDbOpenHelper.getValue("times")
                RxToast.normal("get $value")
            } else {
                CommonDbOpenHelper.setKeyValue("times", times.toString())
                RxToast.normal("set value $times")
            }
            times++
        }
    }

}
