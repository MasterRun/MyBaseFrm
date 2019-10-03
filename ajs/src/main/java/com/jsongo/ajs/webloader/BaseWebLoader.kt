package com.jsongo.ajs.webloader

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import com.jsongo.ajs.R
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.core.mvp.base.BaseFragment
import com.safframework.log.L
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.activity_ajs_webloader.*
import kotlinx.android.synthetic.main.activity_ajs_webloader.view.*

/**
 * @author ： jsongo
 * @date ： 19-10-3 下午5:14
 * @desc : modify from RxUI activitywebloader  mixin jsbridge replace by tencent x5 core
 */
abstract class BaseWebLoader : BaseFragment() {

    /**
     * 加载的url
     */
    var webPath = ""

    override var mainLayoutId = R.layout.activity_ajs_webloader

    protected lateinit var pbWebview: ProgressBar
    protected lateinit var bridgeWebView: BridgeWebView

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
        bridgeWebView = view.bridgeWebView
    }

    protected fun initData() {
        pb_webview.max = 100//设置加载进度最大值
        val webSettings = bridgeWebView.settings
        webSettings.userAgentString = webSettings.userAgentString + "@${this.javaClass}"
        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.cacheMode = WebSettings.LOAD_NO_CACHE//加载缓存否则网络
        }

        if (Build.VERSION.SDK_INT >= 19) {
            webSettings.loadsImagesAutomatically = true//图片自动缩放 打开
        } else {
            webSettings.loadsImagesAutomatically = false//图片自动缩放 关闭
        }

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            bridgeWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)//软件解码
//        }
//        bridgeWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)//硬件解码

        //允许webview对文件的操作
        webSettings.setAllowUniversalAccessFromFileURLs(true)
        webSettings.allowFileAccess = true
        webSettings.setAllowFileAccessFromFileURLs(true)

        webSettings.allowContentAccess = true
        webSettings.setAppCacheEnabled(true)
        //开启加载https
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
        }

        // setMediaPlaybackRequiresUserGesture(boolean require) //是否需要用户手势来播放Media，默认true

        // 设置支持javascript脚本
        webSettings.javaScriptEnabled = true
        //webSettings.setPluginState(WebSettings.PluginState.ON);

        // 设置可以支持缩放
        webSettings.setSupportZoom(false)
        // 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
        webSettings.builtInZoomControls = false

        //隐藏缩放工具
        webSettings.displayZoomControls = false
        // 扩大比例的缩放
        webSettings.useWideViewPort = true

        //自适应屏幕
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webSettings.loadWithOverviewMode = true

        webSettings.databaseEnabled = true//
        //保存密码
        webSettings.savePassword = true
        //是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
        webSettings.domStorageEnabled = true
        bridgeWebView.isSaveEnabled = true
        bridgeWebView.keepScreenOn = true

        // 设置setWebChromeClient对象
        bridgeWebView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                topbar.setTitle(title)
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                pb_webview.progress = newProgress
                super.onProgressChanged(view, newProgress)
            }
        }

        bridgeWebView.setOnPageFinishListener { view, url ->
            onLoadFinish(view, url)
        }

        bridgeWebView.setDownloadListener { paramStr1, paramStr2, paramStr3, paramStr4, paramLong ->
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse(paramStr1)
            startActivity(intent)
        }

        bridgeWebView.loadUrl(webPath)
        L.d("url ", webPath)

        //ridgeWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,bridgeWebView.getHeight()));

        registerHandler()
    }

    protected abstract fun registerHandler()

    /**
     * 页面加载完成
     */
    protected open fun onLoadFinish(wv: WebView, url: String) {
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
        if (bridgeWebView.canGoBack()) {
            bridgeWebView.goBack()
            return true
        }
        return false
    }

}