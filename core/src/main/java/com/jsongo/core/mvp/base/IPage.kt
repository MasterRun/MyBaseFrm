package com.jsongo.core.mvp.base

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.jsongo.core.annotations.ConfPageProcessor
import com.jsongo.core.util.SmartRefreshFooter
import com.jsongo.core.util.SmartRefreshHeader
import com.jsongo.core.util.useFooter
import com.jsongo.core.util.useHeader
import com.jsongo.core.widget.TopbarLayout
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.scwang.smartrefresh.layout.SmartRefreshLayout

/**
 * author ： jsongo
 * createtime ： 2019/7/27 11:38
 * desc : 页面接口
 */
interface IPage {
    /**
     * 根布局
     */
    val clLayoutContainer: ConstraintLayout

    /**
     * 标题栏
     */
    val topbar: TopbarLayout

    /**
     * 下拉刷新
     */
    val smartRefreshLayout: SmartRefreshLayout

    /**
     * 滚动布局
     */
    val nsv: NestedScrollView

    /**
     *容器1
     */
    val flMainContainer: FrameLayout

    /**
     *容器2
     */
    val flMainContainer2: FrameLayout

    /**
     *容器3
     */
    val flMainContainer3: FrameLayout

    /**
     * 空页面 状态页
     */

    val emptyView: QMUIEmptyView
    /**
     * 使用的容器下标
     */

    val containerIndex: Int
        get() = 1

    /**
     * 布局资源id
     */
    val mainLayoutId: Int

    fun getIPageView()

    fun initIPage(context: Context) {

        //获取view
        getIPageView()

        emptyView.visibility = View.GONE

        //开启注解
        ConfPageProcessor.config(this)

        if (mainLayoutId == 0) {
            throw  Exception("layout id of (${this}) can not be null")
        }

        val mainView = LayoutInflater.from(context).inflate(mainLayoutId, null)
        //添加主内容到界面
        topbar.visibility = View.VISIBLE
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
                flMainContainer3.addView(mainView)
            }
            else -> {
                smartRefreshLayout.visibility = View.VISIBLE
                flMainContainer2.visibility = View.GONE
                flMainContainer3.visibility = View.GONE
                flMainContainer.addView(mainView)
            }
        }
        //初始化下拉刷新
        smartRefreshLayout
            .useHeader(context, SmartRefreshHeader.BezierCircleHeader)
            .useFooter(context, SmartRefreshFooter.ClassicsFooter)

    }

    fun onIPageDestory() {
        emptyView.hide()
        smartRefreshLayout.finishRefresh().finishLoadMore()
    }
}