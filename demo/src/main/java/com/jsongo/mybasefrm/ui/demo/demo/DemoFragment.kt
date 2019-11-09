package com.jsongo.mybasefrm.ui.demo.demo

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.view.View
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.core.base.mvvm.stateful.StatefulFragment
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.demo.DemoViewModel
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_demo.*

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:39
 * desc :
 */
@Page(R.layout.activity_demo, 1)
class DemoFragment : StatefulFragment() {

    lateinit var demoViewModel: DemoViewModel

    override fun initViewModel() {
        demoViewModel = ViewModelProviders.of(this).get(DemoViewModel::class.java)
    }

    override fun initView() {
        topbar.visibility = View.GONE
        view?.apply {

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
    }

    override fun onPageReloading() {
        super.onPageReloading()
        demoViewModel.getAuthtypes()
    }
}