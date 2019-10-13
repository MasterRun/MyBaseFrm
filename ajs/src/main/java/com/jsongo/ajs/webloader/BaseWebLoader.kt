package com.jsongo.ajs.webloader

import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.jsongo.ajs.R
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.AJsWebView
import com.jsongo.core.mvp.base.BaseFragment
import com.safframework.log.L
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.activity_ajs_webloader.*
import kotlinx.android.synthetic.main.activity_ajs_webloader.view.*

/**
 * @author ： jsongo
 * @date ： 19-10-3 下午5:14
 * @desc : modify from RxUI activitywebloader  mixin jsbridge replace by tencent x5 core
 */
abstract class BaseWebLoader : BaseFragment(), AjsWebViewHost {

    /**
     * 加载的url
     */
    var webPath = ""

    override var mainLayoutId = R.layout.activity_ajs_webloader

    protected lateinit var pbWebview: ProgressBar
    protected lateinit var aJsWebView: AJsWebView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        // 初始化控件 - findViewById之类的操作
        initView(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // 初始化控件的数据及监听事件
        initData()
    }

    protected abstract fun init()

    protected open fun initView(view: View) {
        pbWebview = view.pb_webview
        aJsWebView = view.bridgeWebView
        aJsWebView.ajsWebViewHost = this

        pbWebview.max = 100//设置加载进度最大值
    }

    protected fun initData() {
        aJsWebView.webPath = webPath
        aJsWebView.loadingProgressListener = object : AJsWebView.LoadingProgressListener {
            override fun onReceiveTitle(wv: WebView?, title: String?) {
                topbar.setTitle(title)
            }

            override fun onProgressChanged(wv: WebView?, newProgress: Int) {
                pbWebview.progress = newProgress
            }

            override fun onLoadFinish(wv: WebView?, url: String) {
                this@BaseWebLoader.onLoadFinish(wv, url)
            }
        }
        aJsWebView.initLoad()
    }

    /**
     * 页面加载完成
     */
    protected open fun onLoadFinish(wv: WebView?, url: String) {
        //页面加载完成后 隐藏加载进度和加载dialog
        pb_webview.visibility = View.GONE
        smartRefreshLayout.finishRefresh()
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        try {
            super.onConfigurationChanged(newConfig)
            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                L.d("Himi", "onConfigurationChanged_ORIENTATION_LANDSCAPE")
            } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                L.d("Himi", "onConfigurationChanged_ORIENTATION_PORTRAIT")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onBackPressed(): Boolean {
        if (aJsWebView.canGoBack()) {
            aJsWebView.goBack()
            return true
        }
        return false
    }

}