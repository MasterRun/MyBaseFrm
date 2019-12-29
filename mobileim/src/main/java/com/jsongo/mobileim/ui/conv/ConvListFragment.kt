package com.jsongo.mobileim.ui.conv

import android.graphics.Color
import android.view.View
import com.jsongo.core.arch.mvvm.stateful.StatefulFragment
import com.jsongo.mobileim.R
import com.jsongo.ui.util.addStatusBarHeightPadding

/**
 * @author ： jsongo
 * @date ： 2019/12/29 13:33
 * @desc : 消息模块 会话列表fragment
 */
class ConvListFragment : StatefulFragment() {

    override var mainLayoutId: Int = R.layout.fragment_conv_list

    override var containerIndex: Int = 1

    override fun initViewModel() {
    }

    override fun initView() {
        topbar.addStatusBarHeightPadding()
        topbar.setBackgroundColor(Color.WHITE)
        topbar.setTitle("消息").setTextColor(Color.BLACK)
        topbar.backImageButton.visibility = View.GONE
//        onPageLoaded()
    }

    override fun observeLiveData() {
    }
}