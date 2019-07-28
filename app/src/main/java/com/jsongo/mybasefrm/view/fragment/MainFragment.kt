package com.jsongo.mybasefrm.view.fragment

import com.jsongo.ajs.webloader.DefaultWebLoader
import com.jsongo.core.annotations.ConfPage
import com.jsongo.core.annotations.Presenter
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.mvp.base.BaseMvpFragment
import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.mvp.IMainModule
import com.jsongo.mybasefrm.presenter.MainModulePresenter
import com.vondear.rxtool.view.RxToast
import kotlinx.android.synthetic.main.activity_main.*

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:39
 * desc :
 */
@ConfPage(R.layout.activity_main, 1)
class MainFragment : BaseMvpFragment<IMainModule.IModel, IMainModule.IView>(), IMainModule.IView {

    @Presenter(MainModulePresenter::class)
    override lateinit var basePresenter: BasePresenter<IMainModule.IModel, IMainModule.IView>
    @Presenter(MainModulePresenter::class)
    lateinit var presenter: IMainModule.IPresenter<IMainModule.IModel, IMainModule.IView>

    override fun initView() {
        view?.apply {

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
        }
    }

    override fun onGetDailyGank(txt: String?) {
        tv.text = txt
    }
}