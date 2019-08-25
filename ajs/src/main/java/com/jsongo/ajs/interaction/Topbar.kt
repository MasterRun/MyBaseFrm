package com.jsongo.ajs.interaction

import android.graphics.Color
import android.view.View
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.jsongo.ajs.Util
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.qmuiteam.qmui.util.QMUIStatusBarHelper

/**
 * @author  jsongo
 * @date 2019/6/18 20:41
 * @desc topbar标题栏
 */
object Topbar {

    /**
     * topbar背景色  "#aaaaaa"
     */
    @JvmStatic
    fun bgcolor(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val colorStr = params["color"]
        jsWebLoader.topbar.setBackgroundColor(Color.parseColor(colorStr))
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * 是否隐藏topbar
     */
    @JvmStatic
    fun hide(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val hide = (params["hide"] ?: "false").toBoolean()
        if (hide) {
            jsWebLoader.topbar.visibility = View.GONE
        } else {
            jsWebLoader.topbar.visibility = View.VISIBLE
        }
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * 设置topbar标题的文字，颜色，字体大小
     */
    @JvmStatic
    fun title(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val topbarTitle = jsWebLoader.topbar.tvTitle
        val text = params["text"]
        if (text != null) {
            topbarTitle.text = text
        }
        val colorStr = params["color"]
        if (colorStr != null) {
            topbarTitle.setTextColor(Color.parseColor(colorStr))
        }
        val textSizeStr = params["size"]
        if (textSizeStr != null) {
            val toDoublet = textSizeStr.toDoubleOrNull()
            toDoublet?.let {
                topbarTitle.textSize = it.toFloat()
            }
        }
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

/*    @JvmStatic
    fun subtitle(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val topbarTitle = jsWebLoader.topbarTitle
        val text = params["text"]
        if (text != null) {
            topbarTitle.text = text
        }
        val colorStr = params["color"]
        if (colorStr != null) {
            topbarTitle.setTextColor(Color.parseColor(colorStr))
        }
        val textSizeStr = params["size"]
        if(textSizeStr!=null){
            val toDoublet = textSizeStr.toDoubleOrNull()
            toDoublet?.let {
                topbarTitle.textSize = it.toFloat()
            }
        }
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }*/

    /**
     * 设置状态栏字体颜色
     */
    @JvmStatic
    fun statusbar(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val mode = (params["mode"] ?: "0").toString().toDouble().toInt()
        if (mode == 1) {
            QMUIStatusBarHelper.setStatusBarDarkMode(jsWebLoader)
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(jsWebLoader)
        }
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }
}