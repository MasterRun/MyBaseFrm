package com.jsongo.ajs.interaction

import android.graphics.Color
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.AJsWebView
import com.jsongo.core.util.*

/**
 * author ： jsongo
 * createtime ： 2019/7/12 15:39
 * desc : 下拉刷新及上拉加载
 */
object SmartRefresh {

    /**
     * 是否启用刷新
     */
    @JvmStatic
    fun enableRefresh(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostIPage = ajsWebViewHost.hostIPage
        if (hostIPage != null) {
            val enable = !params["enable"].equals("false")
            hostIPage.smartRefreshLayout.setEnableRefresh(enable)
            callback.success()
        } else {
            callback.failure()
        }
    }

    /**
     * 是否启用加载更多
     */
    @JvmStatic
    fun enableLoadmore(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostIPage = ajsWebViewHost.hostIPage
        if (hostIPage != null) {
            val enable = !params["enable"].equals("false")
            hostIPage.smartRefreshLayout.setEnableLoadMore(enable)
            callback.success()
        } else {
            callback.failure()
        }
    }

    /**
     * 设置动画颜色
     */
    @JvmStatic
    fun color(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostIPage = ajsWebViewHost.hostIPage
        if (hostIPage != null) {
            val primaryColorStr = params["primaryColor"].toString()
            val accentColor = params["accentColor"].toString()

            hostIPage.smartRefreshLayout.userColors(
                Color.parseColor(primaryColorStr),
                Color.parseColor(accentColor)
            )
            callback.success()
        } else {
            callback.failure()
        }
    }

    /**
     * 设置刷新头
     */
    @JvmStatic
    fun header(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostIPage = ajsWebViewHost.hostIPage
        if (hostIPage != null) {
            val context = ajsWebViewHost.hostActivity
            if (context == null) {
                callback.failure(message = "context of webLoader id null")
                return
            }
            val header = params["header"].toString()
            hostIPage.smartRefreshLayout.useHeader(context, SmartRefreshHeader.valueOf(header))

            callback.success()
        } else {
            callback.failure()
        }
    }

    /**
     * 设置加载更多动画
     */
    @JvmStatic
    fun footer(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostIPage = ajsWebViewHost.hostIPage
        if (hostIPage != null) {
            val context = ajsWebViewHost.hostActivity
            if (context == null) {
                callback.failure(message = "context of webLoader id null")
                return
            }
            val footer = params["footer"].toString()
            hostIPage.smartRefreshLayout.useFooter(context, SmartRefreshFooter.valueOf(footer))

            callback.success()
        } else {
            callback.failure()
        }
    }


}