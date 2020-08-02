package com.jsongo.core.arch

import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.widget.NestedScrollView
import com.jsongo.core.R
import com.jsongo.core.widget.StatusView
import com.jsongo.core.widget.TopbarLayout
import com.jsongo.core_mini.base_page.BaseFragment
import com.jsongo.core_mini.widget.ILoadingDialog
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.main.layout_frm_base.view.*

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc fragment 父类
 */
abstract class BaseFragmentWrapper : BaseFragment(), IPageWrapper {
    override lateinit var rlLayoutRoot: RelativeLayout
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
    override lateinit var vsEmptyView: ViewStub
        protected set
    override var statusView: StatusView? = null
        protected set
    override var loadingDialog: ILoadingDialog? = null

    override lateinit var mainView: View

    override var mainLayoutId = 0

    override val layoutId: Int = R.layout.layout_frm_base

    override var containerIndex = 0

    override fun getIPageView() {
        view?.let {
            rlLayoutRoot = it.rl_layout_root
            topbar = it.findViewById(R.id.topbar)
            smartRefreshLayout = it.smart_refresh_layout
            nsv = it.findViewById(R.id.nsv)
            flMainContainer = it.fl_main_container
            flMainContainer2 = it.fl_main_container2
            vsEmptyView = it.vs_emptyview
        }
    }

    override fun inflateEmptyView(): StatusView? {
        if (statusView == null) {
            statusView = vsEmptyView.inflate().findViewById(R.id.empty_view)
        }
        return statusView
    }

    override fun onDestroyIPage() {
        super<IPageWrapper>.onDestroyIPage()
        super<BaseFragment>.onDestroyIPage()
    }
}