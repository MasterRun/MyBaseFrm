package com.jsongo.mybasefrm.view.activity

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.ajs.webloader.DefaultWebLoader
import com.jsongo.annotation.anno.AjsApi
import com.jsongo.annotation.anno.ConfPage
import com.jsongo.annotation.anno.Presenter
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.mvp.base.BaseMvpActivity
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.core.util.ActivityCollector
import com.jsongo.core.util.SmartRefreshHeader
import com.jsongo.core.util.initWithStr
import com.jsongo.core.util.useHeader
import com.jsongo.core.view.activity.SplashActivity
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.mvp.IMain
import com.jsongo.mybasefrm.presenter.MainPresenter
import com.jsongo.mybasefrm.view.fragment.MainFragment
import com.jsongo.mybasefrm.view.fragment.MyPageFragment
import com.jsongo.ui.widget.FloatingView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.vondear.rxtool.view.RxToast
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.properties.Delegates

@ConfPage(R.layout.activity_main, 2)
class MainActivity : BaseMvpActivity<IMain.IModel, IMain.IView>(), IMain.IView {

    @Presenter(MainPresenter::class)
    override lateinit var basePresenter: BasePresenter<IMain.IModel, IMain.IView>
    @Presenter(MainPresenter::class)
    lateinit var presenter: IMain.IPresenter<IMain.IModel, IMain.IView>

    var txt: String by Delegates.observable("init value") { prop, old, newValue ->
        tv.text = newValue
    }

    companion object zzz {

        //测试
        @AjsApi(prefix = "custom3")
        @JvmStatic
        fun toast(
            jsWebLoader: AJsWebLoader,
            bridgeWebView: BridgeWebView,
            params: Map<String, String>, callback: AjsCallback
        ) {
            val text = params["text"]
            if (TextUtils.isEmpty(text)) {
                callback.failure(HashMap(), 0, "")
                return
            }
            RxToast.error(text!!)
            callback.success(HashMap(), 1, "success")
        }
    }

    override fun initView() {

        setSwipeBackEnable(false)

        val floatingView = FloatingView(this)
        floatingView.show()

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

/*        topbar.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity::class.java)
            startActivity(intent)
        }*/

        btn.setOnClickListener {
            val webPath = "file:///android_asset/web/index.html"

            DefaultWebLoader.load(webPath)
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

        btn_crash.setOnClickListener {
            val a = 0
            println(2 / a)
        }

        btn_goMyPage.setOnClickListener {
            startActivity(Intent(this@MainActivity, MyPageActivity::class.java))
        }


        val mainFragment = MainFragment()
        val myPageFragment = MyPageFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fl_fragment_container, mainFragment, "MainFragment")
        transaction.add(R.id.fl_fragment_container, myPageFragment, "MyPageFragment")
        transaction.commit()

        fun showMainFragment() {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.hide(myPageFragment)
            transaction.show(mainFragment)
            transaction.commit()
        }

        fun showMyPageFragment() {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.hide(mainFragment)
            transaction.show(myPageFragment)
            transaction.commit()
        }

        btn_change.setOnClickListener {

            if (times % 2 == 0) {
                showMyPageFragment()
            } else {
                showMainFragment()
            }
            times++
        }

/*        btn.visibility = View.GONE
        btn_loadbaidu.visibility = View.GONE
        btn_testdb.visibility = View.GONE
        btn_crash.visibility = View.GONE
        tv.visibility = View.GONE*/
        showMainFragment()

        ActivityCollector.finish(SplashActivity::class.java)
    }


    override fun onGetDailyGank(txt: String?) {
//        tv.text = txt
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
        if (requestCode == FloatingView.SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val str = data.getStringExtra(Constant.CODED_CONTENT)
                if (str.startsWith("http://") || str.startsWith("https://")) {
                    DefaultWebLoader.load(str)
                } else {
                    QMUIDialog.MessageDialogBuilder(this@MainActivity)
                        .setTitle("扫描结果")
                        .setMessage(str)
                        .addAction("OK", object : QMUIDialogAction.ActionListener {
                            override fun onClick(dialog: QMUIDialog?, index: Int) {
                                dialog?.dismiss()
                            }
                        }).show()
                }
            }
        }
    }
}
