package com.jsongo.ajs.interaction

import android.graphics.Color
import android.view.View
import com.jsongo.ajs.helper.AjsCallback
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
        callback: AjsCallback
    ) {
        val colorStr = params["color"]
        jsWebLoader.topbar.setBackgroundColor(Color.parseColor(colorStr))
        callback.success()
    }

    /**
     * 是否隐藏topbar
     */
    @JvmStatic
    fun hide(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hide = (params["hide"] ?: "false").toBoolean()
        if (hide) {
            jsWebLoader.topbar.visibility = View.GONE
        } else {
            jsWebLoader.topbar.visibility = View.VISIBLE
        }
        callback.success()
    }

    /**
     * 设置topbar标题的文字，颜色，字体大小
     */
    @JvmStatic
    fun title(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
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
        callback.success()
    }

    /* @JvmStatic
     fun subtitle(
         jsWebLoader: AJsWebLoader,
         bridgeWebView: BridgeWebView,
         params: Map<String, String>,
         callback: AjsCallback
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
         callback.success()
     }*/

    /**
     * 设置状态栏字体颜色
     */
    @JvmStatic
    fun statusbar(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val mode = (params["mode"] ?: "0").toString().toDouble().toInt()
        if (mode == 1) {
            QMUIStatusBarHelper.setStatusBarDarkMode(jsWebLoader.activity)
        } else {
            QMUIStatusBarHelper.setStatusBarLightMode(jsWebLoader.activity)
        }
        callback.success()
    }
}