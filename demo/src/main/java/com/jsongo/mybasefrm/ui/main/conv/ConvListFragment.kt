package com.jsongo.mybasefrm.ui.main.conv

import android.graphics.Color
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.jsongo.annotation.anno.Page
import com.jsongo.core.arch.mvvm.stateful.StatefulFragment
import com.jsongo.core.arch.mvvm.stateful.Status
import com.jsongo.core.widget.RxToast
import com.jsongo.mybasefrm.R
import com.jsongo.mybasefrm.ui.main.MainActivity
import com.jsongo.mybasefrm.ui.main.conv.adapter.ConvItemAdapter
import com.jsongo.ui.util.addStatusBarHeightPadding
import kotlinx.android.synthetic.main.fragment_conv_list.*

/**
 * @author ： jsongo
 * @date ： 2019/12/29 13:33
 * @desc : 消息模块 会话列表fragment
 */
@Page(R.layout.fragment_conv_list)
class ConvListFragment : StatefulFragment() {

    var convItemAdapter: ConvItemAdapter? = null

    lateinit var convListViewModel: ConvListViewModel

    override fun initViewModel() {
        convListViewModel = ViewModelProviders.of(this).get(ConvListViewModel::class.java)
        lifecycle.addObserver(convListViewModel)
    }

    override fun initView() {

        rlLayoutRoot.setBackgroundColor(Color.WHITE)

        topbar.addStatusBarHeightPadding()
        topbar.setBackgroundColor(Color.WHITE)
        topbar.setTitle("消息").setTextColor(Color.BLACK)
        topbar.backImageButton.visibility = View.GONE

        (rv_convs.itemAnimator as SimpleItemAnimator).apply {
            supportsChangeAnimations = false
            changeDuration = 0
        }

        //下拉刷新
        smartRefreshLayout.setOnRefreshListener {
            pageStatus = Status.LOADING
            convListViewModel.getConvList()
        }
    }

    override fun observeLiveData() {
        convListViewModel.convs.observe(this, Observer {
            if (it == null) {
                return@Observer
            }
            val context = context
            if (context != null && convItemAdapter == null) {
                convItemAdapter = ConvItemAdapter(context, it)
                val rv_convs = rv_convs
                rv_convs?.layoutManager = LinearLayoutManager(context)
                rv_convs?.adapter = convItemAdapter
            } else if (convItemAdapter != null) {
                convItemAdapter?.dataList = it
            }
            onPageLoaded()
        })

        //未读数量
        convListViewModel.totalUnreadCount.observe(this, Observer {
            val activity = activity
            if (activity is MainActivity) {
                val index = activity.fragments.indexOf(this)
                if (index >= 0 && index < activity.fragments.size) {
                    activity.mainViewModel.setMainTabTipCount(2, it)
                }
            }
        })

        convListViewModel.errorMessage.observe(this, Observer {
            if (pageStatus == Status.LOADING) {
                onPageLoaded()
            }
            RxToast.error(it)
        })
    }

    override fun onPageReloading() {
        super.onPageReloading()
        convListViewModel.getConvList()
    }

}