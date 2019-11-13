package com.jsongo.core.arch.mvvm.stateful

/**
 * author ： jsongo
 * createtime ： 2019/7/25 10:22
 * desc : 状态管理
 */
interface PageStatusListener {

    var pageStatus: Status

    /**
     * activity打开加载 使用loading dialog
     * fragment打开加载 使用emptyview 显示加载，
     * 下拉刷新时使用activity的loading dialog
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

    fun onPageError() {
        onPageError("")
    }

    fun onPageError(msg: String?)

}

/**
 * 页面状态
 */
enum class Status {
    LOADING,
    SHOWING_DATA,
    RELOADING,
    NO_DATA,
    ERROR
}