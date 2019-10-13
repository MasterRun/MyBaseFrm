package com.jsongo.ajs.interaction

import android.graphics.Color
import android.view.View
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.AJsWebView
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
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostIPage = ajsWebViewHost.hostIPage
        if (hostIPage != null) {
            val colorStr = params["color"]
            hostIPage.topbar.setBackgroundColor(Color.parseColor(colorStr))
            callback.success()
        } else {
            callback.failure()
        }
    }

    /**
     * 是否隐藏topbar
     */
    @JvmStatic
    fun hide(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostIPage = ajsWebViewHost.hostIPage
        if (hostIPage != null) {
            val hide = (params["hide"] ?: "false").toBoolean()
            if (hide) {
                hostIPage.topbar.visibility = View.GONE
            } else {
                hostIPage.topbar.visibility = View.VISIBLE
            }
            callback.success()
        } else {
            callback.failure()
        }
    }

    /**
     * 设置topbar标题的文字，颜色，字体大小
     */
    @JvmStatic
    fun title(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostIPage = ajsWebViewHost.hostIPage
        if (hostIPage != null) {
            val topbarTitle = hostIPage.topbar.tvTitle
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
        } else {
            callback.failure()
        }
    }

    /* @JvmStatic
     fun subtitle(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
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
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = ajsWebViewHost.hostActivity
        if (hostActivity != null) {
            val mode = (params["mode"] ?: "0").toString().toDouble().toInt()
            if (mode == 1) {
                QMUIStatusBarHelper.setStatusBarDarkMode(hostActivity)
            } else {
                QMUIStatusBarHelper.setStatusBarLightMode(hostActivity)
            }
            callback.success()
        } else {
            callback.failure(message = "activity is null!")
        }
    }

    @JvmStatic
    fun statusbarHeight(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = ajsWebViewHost.hostActivity
        if (hostActivity != null) {
            val statusbarHeight = QMUIStatusBarHelper.getStatusbarHeight(hostActivity)
            callback.success(Pair("height", statusbarHeight))
        } else {
            callback.failure(message = "activity is null!")
        }
    }
}