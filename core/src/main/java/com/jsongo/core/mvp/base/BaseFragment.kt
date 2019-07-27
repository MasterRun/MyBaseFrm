package com.jsongo.core.mvp.base

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.jsongo.core.R
import com.jsongo.core.widget.TopbarLayout
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.layout_frm_base.view.*

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc fragment 父类
 */
abstract class BaseFragment : Fragment(), IPage {
    override lateinit var clLayoutContainer: ConstraintLayout
        protected set
    override lateinit var topbar: TopbarLayout
        protected set
    override lateinit var smartRefreshLayout: SmartRefreshLayout
        protected set
    override lateinit var nsv: NestedScrollView
        protected set
    override lateinit var flMainContainer: FrameLayout
        protected set
    override lateinit var flMainContainer2: FrameLayout
        protected set
    override lateinit var flMainContainer3: FrameLayout
        protected set
    override lateinit var emptyView: QMUIEmptyView
        protected set

    override var mainLayoutId = 0
        protected set

    override var containerIndex = 3
        protected set


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val contentView = inflater.inflate(R.layout.layout_frm_base, container, false)

        initIPage(context!!)

        return contentView;
    }

    override fun getIPageView() {
        view?.let {
            clLayoutContainer = it.cl_layout_container
            topbar = it.findViewById(R.id.topbar)
            smartRefreshLayout = it.smart_refresh_layout
            nsv = it.findViewById(R.id.nsv)
            flMainContainer = it.fl_main_container
            flMainContainer2 = it.fl_main_container2
            flMainContainer3 = it.fl_main_container3
            emptyView = it.empty_view
        }
    }

    override fun onDestroy() {
        onIPageDestory()
        view?.clearFindViewByIdCache()
        clearFindViewByIdCache()
        super.onDestroy()
    }
}