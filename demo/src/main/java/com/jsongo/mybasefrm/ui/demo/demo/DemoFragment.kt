package com.jsongo.mybasefrm.ui.demo.demo

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.huantansheng.easyphotos.EasyPhotos
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.mvvm.stateful.StatefulFragment
import com.jsongo.core.db.CommonDbOpenHelper
import com.jsongo.core.constant.ConstConf
import com.jsongo.core.widget.RxToast
import com.jsongo.mybasefrm.BR
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.databinding.ActivityDemoBinding
import com.jsongo.mybasefrm.ui.demo.DemoViewModel
import com.jsongo.mybasefrm.ui.main.MainActivity
import com.jsongo.mybasefrm.ui.mypage.MyPageActivity
import com.jsongo.ui.util.EasyPhotoGlideEngine
import kotlinx.android.synthetic.main.activity_demo.*

/**
 * author ： jsongo
 * createtime ： 2019/7/28 9:39
 * desc :
 */
@Page(R.layout.activity_demo, 1)
class DemoFragment : StatefulFragment() {

    lateinit var demoViewModel: DemoViewModel
    lateinit var activityDemoBinding: ActivityDemoBinding

    override fun initViewModel() {
        demoViewModel = ViewModelProviders.of(this).get(DemoViewModel::class.java)
    }

    override fun initView() {
        topbar.visibility = View.GONE
    }

    override fun observeLiveData() {
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

    override fun bindData() {
        activityDemoBinding = ActivityDemoBinding.bind(mainView)
        activityDemoBinding.setVariable(
            BR.eventProxy,
            EventProxy(this, demoViewModel, activityDemoBinding)
        )
    }

    override fun onPageReloading() {
        super.onPageReloading()
        demoViewModel.getAuthtypes()
    }

    /**
     * 点击事件代理
     */
    class EventProxy(
        private val demoFragment: DemoFragment,
        viewModel: DemoViewModel,
        demoBinding: ActivityDemoBinding
    ) :
        DemoViewModel.EventProxy(viewModel, demoBinding) {
        override fun goMyPage() {
            demoFragment.startActivity(Intent(demoFragment.context, MyPageActivity::class.java))
        }

        override fun choosePhoto() {
            EasyPhotos.createAlbum(demoFragment, true, EasyPhotoGlideEngine.getInstance())
                .setFileProviderAuthority(ConstConf.FILE_PROVIDER_AUTH)
                .setSelectedPhotoPaths(arrayListOf("/storage/emulated/0/ADM/face1.jpg"))
                .start(101)
        }

        override fun goActivity2() {
            demoFragment.startActivity(Intent(demoFragment.context, MainActivity::class.java))
        }

        override fun changeFragment() {
            RxToast.error("无法在fragment中切换fragment")
        }
    }
}