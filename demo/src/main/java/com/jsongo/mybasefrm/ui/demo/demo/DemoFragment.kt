package com.jsongo.mybasefrm.ui.demo.demo

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.mvvm.stateful.StatefulFragment
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.widget.RxToast
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.demo.DemoViewModel
import kotlinx.android.synthetic.main.activity_demo.*

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:39
 * desc :
 */
@Page(R.layout.activity_demo, 1)
class DemoFragment : StatefulFragment() {

    lateinit var demoViewModel: DemoViewModel

    lateinit var clickProxy: ClickProxy

    override fun initViewModel() {
        demoViewModel = ViewModelProviders.of(this).get(DemoViewModel::class.java)
        clickProxy = ClickProxy(demoViewModel)
    }

    override fun initView() {
        topbar.visibility = View.GONE
        view?.apply {

            btn_jsloader.setOnClickListener {
                val webPath = "file:///android_asset/web/index.html"
                clickProxy.loadUrl(webPath)
            }

            btn_loadbaidu.setOnClickListener {
                val webPath = "https://www.baidu.com"
                clickProxy.loadUrl(webPath)
            }

            btn_testdb.setOnClickListener {
                clickProxy.clickTestDb()
            }

            btn_crash.setOnClickListener {
                val a = 0
                println(2 / a)
            }
        }
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

        //监听次数
        demoViewModel.testDbCount.observe(this, Observer {
            val times = it ?: 0
            if (times % 2 == 0) {
                val value = CommonDbOpenHelper.getValue("times")
                RxToast.normal("get $value")
            } else {
                CommonDbOpenHelper.setKeyValue("times", times.toString())
                RxToast.normal("set value $times")
            }

        })
    }

    override fun onPageReloading() {
        super.onPageReloading()
        demoViewModel.getAuthtypes()
    }

    /**
     * 点击事件代理
     */
    class ClickProxy(private val viewModel: DemoViewModel) {

        fun loadUrl(url: String) {
            AJsWebPage.load(url)
        }

        fun clickTestDb() {
            val times = viewModel.testDbCount.value ?: 0
            viewModel.testDbCount.value = times + 1
        }
    }
}