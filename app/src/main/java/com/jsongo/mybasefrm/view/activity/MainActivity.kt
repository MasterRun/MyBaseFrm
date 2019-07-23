package com.jsongo.mybasefrm.view.activity

import android.content.Intent
import android.view.View
import com.jsongo.ajs.webloader.DefaultWebLoader
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.mvp.base.BaseMvpActivity
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.core.util.SmartRefreshHeader
import com.jsongo.core.util.initWithStr
import com.jsongo.core.util.useHeader
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.mvp.IMain
import com.jsongo.mybasefrm.presenter.MainPresenter
import com.vondear.rxfeature.activity.ActivityScanerCode
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseMvpActivity<IMain.IModel, IMain.IView>(), IMain.IView {

    override var basePresenter: BasePresenter<IMain.IModel, IMain.IView>? = null
        private set

    private lateinit var presenter: IMain.IPresenter<IMain.IModel, IMain.IView>

    override fun initPresenter() {
        val mainPresenter = MainPresenter(this)
        basePresenter = mainPresenter
        presenter = mainPresenter
    }

    override var mainLayoutId = R.layout.activity_main

    override fun initView() {
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
