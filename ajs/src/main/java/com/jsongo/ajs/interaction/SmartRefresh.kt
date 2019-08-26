package com.jsongo.ajs.interaction

import android.graphics.Color
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
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
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val enable = !params["enable"].equals("false")
        jsWebLoader.smartRefreshLayout.setEnableRefresh(enable)
        callback.success()
    }

    /**
     * 是否启用加载更多
     */
    @JvmStatic
    fun enableLoadmore(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val enable = !params["enable"].equals("false")
        jsWebLoader.smartRefreshLayout.setEnableLoadMore(enable)
        callback.success()
    }

    /**
     * 设置动画颜色
     */
    @JvmStatic
    fun color(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val primaryColorStr = params["primaryColor"].toString()
        val accentColor = params["accentColor"].toString()

        jsWebLoader.smartRefreshLayout.userColors(
            Color.parseColor(primaryColorStr),
            Color.parseColor(accentColor)
        )
        callback.success()
    }

    /**
     * 设置刷新头
     */
    @JvmStatic
    fun header(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val header = params["header"].toString()
        jsWebLoader.smartRefreshLayout.useHeader(jsWebLoader, SmartRefreshHeader.valueOf(header))

        callback.success()
    }

    /**
     * 设置加载更多动画
     */
    @JvmStatic
    fun footer(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val footer = params["footer"].toString()
        jsWebLoader.smartRefreshLayout.useFooter(jsWebLoader, SmartRefreshFooter.valueOf(footer))

        callback.success()
    }


}