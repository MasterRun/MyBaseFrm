package com.jsongo.app.view.activity

import android.app.Activity
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.annotation.anno.Presenter
import com.jsongo.app.mvp.IMain
import com.jsongo.app.presenter.MainPresenter
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.mvp.base.BaseMvpActivity
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.core.util.ActivityCollector
import com.jsongo.core.util.SmartRefreshHeader
import com.jsongo.core.util.initWithStr
import com.jsongo.core.util.useHeader
import com.jsongo.core.view.activity.SplashActivity
import com.jsongo.ui.component.zxing.Constant
import com.jsongo.ui.widget.FloatingView
import com.jsongo.ui.widget.FloatingView.SCAN_REQUEST_CODE
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.vondear.rxtool.RxRegTool
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.properties.Delegates

@Page(com.jsongo.app.R.layout.activity_main, 2)
class MainActivity : BaseMvpActivity<IMain.IModel, IMain.IView>(), IMain.IView {

    @Presenter(MainPresenter::class)
    override lateinit var basePresenter: BasePresenter<IMain.IModel, IMain.IView>
    @Presenter(MainPresenter::class)
    lateinit var presenter: IMain.IPresenter<IMain.IModel, IMain.IView>

    var txt: String by Delegates.observable("init value") { prop, old, newValue ->
        tv.text = newValue
    }
    var floatingView: FloatingView? = null

    override fun initView() {

        setSwipeBackEnable(false)

        floatingView = FloatingView(this)
        floatingView?.show()

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
            AJsWebPage.load(webPath)
        }

        btn_loadbaidu.setOnClickListener {
            AJsWebPage.load("https://www.baidu.com")
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

        btn_crash.setOnClickListener {
            val a = 0
            println(2 / a)
        }

        btn_goMyPage.setOnClickListener {
            startActivity(Intent(this@MainActivity, MyPageActivity::class.java))
        }

        ActivityCollector.finish(SplashActivity::class.java)
    }


    override fun onGetDailyGank(txt: String?) {
        this.txt = txt ?: ""
    }

    override fun onPageReloading() {
        super.onPageReloading()
        presenter.start()
    }

    /**
     * 设置返回键不关闭应用,回到桌面
     *
     * @param keyCode
     * @param event
     * @return
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if ((keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) && event.action == KeyEvent.ACTION_DOWN) {
            val backHome = Intent(Intent.ACTION_MAIN)
            backHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            backHome.addCategory(Intent.CATEGORY_HOME)
            startActivity(backHome)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 扫描二维码/条码回传
        if (requestCode == SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val str = data.getStringExtra(Constant.CODED_CONTENT)
                if (RxRegTool.isURL(str)) {
                    AJsWebPage.load(str)
                } else {
                    QMUIDialog.MessageDialogBuilder(this@MainActivity)
                        .setTitle("扫描结果")
                        .setMessage(str)
                        .addAction("OK") { dialog, index -> dialog?.dismiss() }
                        .show()
                }
            }
        }
    }

    override fun onDestroy() {
        floatingView?.destory()
        super.onDestroy()
    }
}
