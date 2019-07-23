package com.jsongo.core.mvp.base

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.NestedScrollView
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.jsongo.core.R
import com.jsongo.core.util.*
import com.jsongo.core.widget.SlidingLayout
import com.jsongo.core.widget.TopbarLayout
import com.qmuiteam.qmui.util.QMUIStatusBarHelper
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_base.*

/**
 * @author  jsongo
 * @date 2019/4/3 20:48
 * @desc activity父类
 */
abstract class BaseActivity : AppCompatActivity() {

    private lateinit var slidingLayout: SlidingLayout
    //loadngDialog
    lateinit var loadingDialog: QMUITipDialog
        protected set

    lateinit var clLayoutContainer: ConstraintLayout
        protected set
    lateinit var topbar: TopbarLayout
        protected set
    lateinit var smartRefreshLayout: SmartRefreshLayout
        protected set
    lateinit var nsv: NestedScrollView
        protected set
    lateinit var flMainContainer: FrameLayout
        protected set
    lateinit var flMainContainer2: FrameLayout
        protected set
    lateinit var flMainContainer3: FrameLayout
        protected set
    lateinit var emptyView: QMUIEmptyView
        protected set

    /**
     * 是否使用第几个容器
     */
    open var containerIndex = 1
        protected set

    /**
     * 布局资源id
     */
    abstract var mainLayoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCollector.addActivity(this)

        setContentView(R.layout.activity_base)

        getView()

        val mainView = LayoutInflater.from(this).inflate(mainLayoutId, null)
        //添加主内容到界面
        topbar.visibility = View.VISIBLE
        emptyView.visibility = View.VISIBLE
        when (containerIndex) {
            2 -> {
                smartRefreshLayout.visibility = View.GONE
                flMainContainer2.visibility = View.VISIBLE
                flMainContainer2.addView(mainView)
            }
            3 -> {
                topbar.visibility = View.GONE
                emptyView.visibility = View.GONE
                flMainContainer2.visibility = View.GONE
                smartRefreshLayout.visibility = View.GONE
                flMainContainer3.visibility = View.VISIBLE
            }
            else -> {
                smartRefreshLayout.visibility = View.VISIBLE
                flMainContainer2.visibility = View.GONE
                flMainContainer3.visibility = View.GONE
                flMainContainer.addView(mainView)
            }
        }

        //loadingDialog
        loadingDialog = QMUITipDialog.Builder(this)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord("加载中...")
            .create()

        //侧滑返回
        slidingLayout = SlidingLayout(this)
        setSwipeBackEnable(true)

        //沉浸/透明状态栏
        QMUIStatusBarHelper.translucent(this)
        QMUIStatusBarHelper.setStatusBarDarkMode(this)

        //初始化下拉刷新
        smartRefreshLayout
            .useHeader(this, SmartRefreshHeader.BezierCircleHeader)
            .useFooter(this, SmartRefreshFooter.ClassicsFooter)
        emptyView.visibility = View.GONE
    }

    private fun getView() {
        clLayoutContainer = cl_layout_container
        topbar = findViewById(R.id.topbar)
        smartRefreshLayout = smart_refresh_layout
        nsv = findViewById(R.id.nsv)
        flMainContainer = fl_main_container
        flMainContainer2 = fl_main_container2
        flMainContainer3 = fl_main_container3
        emptyView = empty_view
    }

    fun setSwipeBackEnable(isEnable: Boolean) {
        if (isEnable) {
            this.slidingLayout.bindActivity(this)
        } else {
            this.slidingLayout.setEnableSlidClose(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        clearFindViewByIdCache()
        ActivityCollector.removeActivity(this)
    }
}