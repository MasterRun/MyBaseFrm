package com.jsongo.mybasefrm.jsloader.interaction

import com.github.lzyzsd.jsbridge.CallBackFunction
import com.jsongo.mybasefrm.jsloader.AJsWebLoader
import com.jsongo.mybasefrm.jsloader.jsbridge.BridgeWebView
import kotlinx.android.synthetic.main.activity_base.*

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
        function: CallBackFunction
    ) {
        val enable = !params["enable"].equals("false")
        jsWebLoader.smart_refresh_layout.setEnableRefresh(enable)
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }
    /**
     * 是否启用加载更多
     */
    @JvmStatic
    fun enableLoadmore(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val enable = !params["enable"].equals("false")
        jsWebLoader.smart_refresh_layout.setEnableLoadMore(enable)
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }
}