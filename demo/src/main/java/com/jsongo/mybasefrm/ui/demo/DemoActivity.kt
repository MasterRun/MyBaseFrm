package com.jsongo.mybasefrm.ui.demo

import android.app.Activity
import android.content.Intent
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
import com.jsongo.core.widget.RxToast
import com.jsongo.mybasefrm.BR
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.aop.AopOnclick
import com.jsongo.mybasefrm.databinding.ActivityDemoBinding
import com.jsongo.mybasefrm.ui.demo.demo.DemoFragment
import com.jsongo.mybasefrm.ui.main.MainActivity
import com.jsongo.mybasefrm.ui.mypage.MyPageActivity
import com.jsongo.mybasefrm.ui.mypage.mypage.MyPageFragment
import com.jsongo.ui.component.zxing.Constant
import com.jsongo.ui.util.EasyPhotoGlideEngine
import com.jsongo.ui.widget.FloatingView
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.safframework.log.L
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

    lateinit var viewModel: DemoViewModel

    lateinit var activityDemoBinding: ActivityDemoBinding

    lateinit var demoFragment: DemoFragment
    lateinit var myPageFragment: MyPageFragment
    lateinit var aJsWebLoader: AJsWebLoader

    override fun initViewModel() {
        viewModel = ViewModelProviders.of(this).get(DemoViewModel::class.java)
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

        initFragment()

/*        btn.visibility = View.GONE
        btn_loadbaidu.visibility = View.GONE
        btn_testdb.visibility = View.GONE
        btn_crash.visibility = View.GONE
        tv.visibility = View.GONE*/

        ActivityCollector.finish(SplashActivity::class.java)

        val javaClass = qab.javaClass.superclass
        L.e(javaClass?.name)

    }

    fun initFragment() {
        demoFragment = DemoFragment()
        myPageFragment = MyPageFragment()
        aJsWebLoader = AJsWebLoader.newInstance("file:///android_asset/web/index.html", false)

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
        showAjsWebLoader()
    }

    override fun observeLiveData() {

        //监听次数
        viewModel.testDbCount.observe(this, Observer {
            val times = it ?: 0
            if (times % 2 == 0) {
                val value = CommonDbOpenHelper.getValue("times")
                RxToast.normal("get $value")
            } else {
                CommonDbOpenHelper.setKeyValue("times", times.toString())
                RxToast.normal("set value $times")
            }
        })

        viewModel.getAuthtypes()
        //监听设置文本
        viewModel.txtContent.observe(this, Observer {
            tv.text = it
            onPageLoaded()
        })

        //监听异常
        viewModel.errorLiverData.observe(this, Observer {
            onPageError(it?.message)
        })
    }

    override fun bindData() {
        activityDemoBinding = ActivityDemoBinding.bind(mainView)
        activityDemoBinding.setVariable(
            BR.eventProxy,
            EventProxy(this, viewModel, activityDemoBinding)
        )
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

    override fun onPageReloading() {
        super.onPageReloading()
        viewModel.getAuthtypes()
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

        if (RegUtil.isMatch(URL_REG, str) || str.trim().startsWith(PRE_ANDROID_ASSET)) {
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

    class EventProxy(
        private val demoActivity: DemoActivity,
        viewModel: DemoViewModel,
        activityDemoBinding: ActivityDemoBinding
    ) : DemoViewModel.EventProxy(viewModel, activityDemoBinding) {
        override fun goMyPage() {
            demoActivity.startActivity(Intent(demoActivity, MyPageActivity::class.java))
        }

        override fun choosePhoto() {
            EasyPhotos.createAlbum(demoActivity, true, EasyPhotoGlideEngine.getInstance())
                .setFileProviderAuthority(ConstConf.FILE_PROVIDER_AUTH)
                .setSelectedPhotoPaths(arrayListOf("/storage/emulated/0/ADM/face1.jpg"))
                .start(101)
        }

        override fun goActivity2() {
            demoActivity.startActivity(Intent(demoActivity, MainActivity::class.java))
        }

        @AopOnclick(1500)
        override fun changeFragment() {
            val times = demoViewModel.changeFragmentTimes.value ?: 0
            val i = times % 3
            when (i) {
                0 -> demoActivity.showMyPageFragment()
                1 -> demoActivity.showMainFragment()
                else -> demoActivity.showAjsWebLoader()
            }
            demoViewModel.changeFragmentTimes.value = times + 1
        }
    }
}
