package com.jsongo.core.arch

import android.os.Bundle
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.core.widget.NestedScrollView
import com.jsongo.core.R
import com.jsongo.core.widget.LoadingDialog
import com.jsongo.core.widget.SlidingLayout
import com.jsongo.core.widget.StatusView
import com.jsongo.core.widget.TopbarLayout
import com.jsongo.core_mini.base_page.BaseActivity
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.layout_frm_base.*

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc activity父类
 */
abstract class BaseActivityWrapper : BaseActivity(), IPageWrapper {

    private lateinit var slidingLayout: SlidingLayout

    //loadingDialog
    override lateinit var loadingDialog: LoadingDialog
        protected set

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
    override lateinit var mainView: View

    override var mainLayoutId = 0

    override var containerIndex = 1

    override val layoutId: Int = R.layout.layout_frm_base

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //loadingDialog
        loadingDialog = LoadingDialog.Builder(this)
            .setIconType(LoadingDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(getString(R.string.dialog_loading))
            .create()

        //侧滑返回
        slidingLayout = SlidingLayout(this)
        setSwipeBackEnable(true)
    }

    /**
     * 获取widget
     */
    override fun getIPageView() {
        rlLayoutRoot = rl_layout_root
        topbar = findViewById(R.id.topbar)
        smartRefreshLayout = smart_refresh_layout
        nsv = findViewById(R.id.nsv)
        flMainContainer = fl_main_container
        flMainContainer2 = fl_main_container2
        vsEmptyView = vs_emptyview
    }

    override fun inflateEmptyView(): StatusView? {
        if (statusView == null) {
            statusView = vsEmptyView.inflate().findViewById(R.id.empty_view)
        }
        return statusView
    }

    /**
     * 侧滑返回
     */
    fun setSwipeBackEnable(isEnable: Boolean) {
        if (isEnable) {
            this.slidingLayout.bindActivity(this)
        } else {
            this.slidingLayout.setEnableSlidClose(false)
        }
    }

    override fun onDestroy() {
        onDestroyIPage()
        loadingDialog.dismiss()
        clearFindViewByIdCache()
        super.onDestroy()
    }

    override fun onDestroyIPage() {
        super<IPageWrapper>.onDestroyIPage()
        super<BaseActivity>.onDestroyIPage()
    }
}