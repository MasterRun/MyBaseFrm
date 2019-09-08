package com.jsongo.core.mvp.base

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.jsongo.core.BaseCore
import com.jsongo.core.annotations.PageConfigor
import com.jsongo.core.util.SmartRefreshFooter
import com.jsongo.core.util.SmartRefreshHeader
import com.jsongo.core.util.useFooter
import com.jsongo.core.util.useHeader
import com.jsongo.core.widget.TopbarLayout
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.safframework.log.L
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
    val rlLayoutRoot: RelativeLayout

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
     * 状态页的viewstub
     */
    val vsEmptyView: ViewStub

    /**
     * 空页面 状态页
     */
    val emptyView: QMUIEmptyView?

    /**
     * 使用的容器下标
     */
    val containerIndex: Int
        get() = 1

    /**
     * 布局资源id
     */
    val mainLayoutId: Int

    /**
     * 获取基础ui控件
     */
    fun getIPageView()

    /**
     * inflate emptyview
     */
    fun inflateEmptyView(): QMUIEmptyView?

    fun initIPage(context: Context) {

        //获取view
        getIPageView()

        //开启注解
        PageConfigor.config(this)

        //添加主内容到界面
        if (mainLayoutId == 0 || containerIndex < 1 || containerIndex > 2) {
            topbar.visibility = View.GONE
            flMainContainer2.visibility = View.GONE
            smartRefreshLayout.visibility = View.GONE
            if (mainLayoutId != 0) {
                val mainView =
                    LayoutInflater.from(context).inflate(mainLayoutId, rlLayoutRoot, false)
                rlLayoutRoot.addView(mainView)
                if (BaseCore.isDebug) {
                    L.w("containerIndex of ${this} is ${containerIndex},use rlLayoutRoot")
                }
            } else {
                if (BaseCore.isDebug) {
                    L.w("mainLayoutId of ${this} is 0")
                }
            }
        } else {
            topbar.visibility = View.VISIBLE
            when (containerIndex) {
                1 -> {
                    val mainView =
                        LayoutInflater.from(context).inflate(mainLayoutId, flMainContainer, false)
                    smartRefreshLayout.visibility = View.VISIBLE
                    flMainContainer2.visibility = View.GONE
                    flMainContainer.addView(mainView)
                    //初始化下拉刷新
                    smartRefreshLayout
                        .useHeader(context, SmartRefreshHeader.BezierCircleHeader)
                        .useFooter(context, SmartRefreshFooter.ClassicsFooter)
                }
                2 -> {
                    val mainView =
                        LayoutInflater.from(context).inflate(mainLayoutId, flMainContainer2, false)
                    smartRefreshLayout.visibility = View.GONE
                    flMainContainer2.visibility = View.VISIBLE
                    flMainContainer2.addView(mainView)
                }
            }
        }

    }

    fun onIPageDestory() {
        emptyView?.hide()
        smartRefreshLayout.finishRefresh().finishLoadMore()
    }
}