package com.jsongo.core.mvp.base

/**
 * author ： jsongo
 * createtime ： 2019/7/25 10:22
 * desc : 状态管理
 */
interface PageStatusListener {

    var pageStatus: Status

    /**
     * 页面首次加载和下拉刷新，使用loading dialog
     */
    fun onPageLoading()

    /**
     * QMUIEmptyView 和LoadingDialog都隐藏
     */
    fun onPageLoaded()

    /**
     * 使用QMUIEmptyView
     */
    fun onPageReloading()

    /**
     * 使用QMUIEmptyView
     */
    fun onPageEmpty()

    /**
     * 使用QMUIEmptyView
     */
    fun onPageError()

}

enum class Status {
    LOADING,
    SHOWING_DATA,
    RELOADING,
    NO_DATA,
    ERROR
}