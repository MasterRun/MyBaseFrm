package com.jsongo.mybasefrm.view.fragment

import android.view.View
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.annotation.anno.Page
import com.jsongo.annotation.anno.Presenter
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.mvp.base.BaseMvpFragment
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.mvp.IMainModule
import com.jsongo.mybasefrm.presenter.MainModulePresenter
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_demo.*

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:39
 * desc :
 */
@Page(R.layout.activity_demo, 1)
class DemoFragment : BaseMvpFragment<IMainModule.IModel, IMainModule.IView>(), IMainModule.IView {

    @Presenter(MainModulePresenter::class)
    override lateinit var basePresenter: BasePresenter<IMainModule.IModel, IMainModule.IView>
    @Presenter(MainModulePresenter::class)
    lateinit var presenter: IMainModule.IPresenter<IMainModule.IModel, IMainModule.IView>

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

    override fun onGetDailyGank(txt: String?) {
        tv.text = txt
    }

    override fun onPageReloading() {
        super.onPageReloading()
        presenter.start()
    }
}