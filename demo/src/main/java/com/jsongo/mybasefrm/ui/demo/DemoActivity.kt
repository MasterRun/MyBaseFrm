package com.jsongo.mybasefrm.ui.demo

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.huantansheng.easyphotos.EasyPhotos
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.webloader.AJsApplet
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.ajs.widget.AJsWebView
import com.jsongo.annotation.anno.AjsApi
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.mvvm.stateful.StatefulActivity
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.ui.splash.SplashActivity
import com.jsongo.core.util.*
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.aop.AopOnclick
import com.jsongo.mybasefrm.ui.demo.demo.DemoFragment
import com.jsongo.mybasefrm.ui.main.MainActivity
import com.jsongo.mybasefrm.ui.mypage.MyPageActivity
import com.jsongo.mybasefrm.ui.mypage.mypage.MyPageFragment
import com.jsongo.ui.component.zxing.Constant
import com.jsongo.ui.util.EasyPhotoGlideEngine
import com.jsongo.ui.widget.FloatingView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.safframework.log.L
import com.vondear.rxtool.RxRegTool
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_demo.*
import java.util.*

@Page(R.layout.activity_demo, 2)
class DemoActivity : StatefulActivity() {

    companion object zzz {

        //测试
        @AjsApi(prefix = "custom3")
        @JvmStatic
        fun toast(
            ajsWebViewHost: AjsWebViewHost,
            aJsWebView: AJsWebView,
            params: Map<String, String>,
            callback: AjsCallback
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

    private var floatingView: FloatingView? = null

    lateinit var demoViewModel: DemoViewModel

    override fun initViewModel() {
        demoViewModel = ViewModelProviders.of(this).get(DemoViewModel::class.java)
    }

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

/*        topbar.setOnClickListener {
            val intent = Intent(this@DemoActivity, DemoActivity::class.java)
            startActivity(intent)
        }*/

        btn_jsloader.setOnClickListener {
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
            startActivity(Intent(this@DemoActivity, MyPageActivity::class.java))
        }

        btn_choosePhoto.setOnClickListener {
            EasyPhotos.createAlbum(
                this@DemoActivity, true,
                EasyPhotoGlideEngine.getInstance()
            )
                .setSelectedPhotoPaths(arrayListOf("/storage/emulated/0/ADM/face1.jpg"))
                .setFileProviderAuthority(ConstConf.FILE_PROVIDER_AUTH)
                .start(101)
        }

        btn_goActivity2.setOnClickListener {
            startActivity(Intent(this@DemoActivity, MainActivity::class.java))
        }

        btn_godemo1.setOnClickListener {
            AJsWebPage.load("file:///android_asset/web/demo1/index.html", false)
        }

        val demoFragment = DemoFragment()
        val myPageFragment = MyPageFragment()
        val aJsWebLoader = AJsWebLoader.newInstance("file:///android_asset/web/index.html", false)

        supportFragmentManager.beginTransaction().apply {
            add(
                R.id.fl_fragment_container,
                demoFragment,
                "DemoFragment"
            )
            add(
                R.id.fl_fragment_container,
                myPageFragment,
                "MyPageFragment"
            )
            add(
                R.id.fl_fragment_container,
                aJsWebLoader,
                "ajsWebloader"
            )
            commit()
        }

        fun showMainFragment() {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.hide(myPageFragment)
            transaction.hide(aJsWebLoader)
            transaction.show(demoFragment)
            transaction.commit()
        }

        fun showMyPageFragment() {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.hide(demoFragment)
            transaction.hide(aJsWebLoader)
            transaction.show(myPageFragment)
            transaction.commit()
        }

        fun showAjsWebLoader() = supportFragmentManager.beginTransaction().apply {
            hide(demoFragment)
            hide(myPageFragment)
            show(aJsWebLoader)
            commit()
        }

        btn_change.setOnClickListener(object : View.OnClickListener {
            @AopOnclick(1500)
            override fun onClick(v: View?) {
                val i = times % 3
                when (i) {
                    0 -> showMyPageFragment()
                    1 -> showMainFragment()
                    else -> showAjsWebLoader()
                }
                times++
            }
        })

/*        btn.visibility = View.GONE
        btn_loadbaidu.visibility = View.GONE
        btn_testdb.visibility = View.GONE
        btn_crash.visibility = View.GONE
        tv.visibility = View.GONE*/
        showAjsWebLoader()

        ActivityCollector.finish(SplashActivity::class.java)
    }

    override fun observeLiveData() {
        demoViewModel.getAuthtypes()
        //监听设置文本
        demoViewModel.txtContent.observe(this, Observer {
            tv.text = it
            onPageLoaded()
        })

        //监听异常
        demoViewModel.errorLiverData.observe(this, Observer {
            onPageError(it?.message)
        })
    }

    override fun onPageReloading() {
        super.onPageReloading()
        demoViewModel.getAuthtypes()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        L.json(data)
        // 扫描二维码/条码回传
        if (requestCode == FloatingView.SCAN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val str = data.getStringExtra(Constant.CODED_CONTENT)
                onScanResult(str)
            }
        }
    }

    /**
     * 扫码回调
     */
    fun onScanResult(str: String) {

        if (RxRegTool.isMatch(URL_REG, str) || str.trim().startsWith(PRE_ANDROID_ASSET)) {
            //加载页面
            AJsApplet.load(str)
        } else {
            try {
                //尝试打开原生页面
                startActivity(Intent(this, Class.forName(str)))
            } catch (e: Exception) {
                e.printStackTrace()
                //弹唱显示接口
                QMUIDialog.MessageDialogBuilder(this@DemoActivity)
                    .setTitle("扫描结果")
                    .setMessage(str)
                    .addAction("打开") { dialog, index ->
                        //WebLoader加载字符串
                        dialog?.dismiss()
                        AJsWebPage.load(str)
                    }
                    .addAction("OK") { dialog, index ->
                        dialog?.dismiss()
                    }.show()
            }
        }
    }

    override fun onDestroy() {
        floatingView?.destory()
        super.onDestroy()
    }
}
