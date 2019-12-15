package com.jsongo.ajs.widget

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.util.AttributeSet
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.helper.InteractionRegisterCollector
import com.jsongo.ajs.jsbridge_x5.BridgeWebView
import com.jsongo.ajs.jsbridge_x5.BridgeWebViewClient
import com.jsongo.core.util.LogcatUtil
import com.jsongo.core.util.PRE_ANDROID_ASSET
import com.jsongo.core.util.SRT_HTTP
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.export.external.interfaces.WebResourceResponse
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView

/**
 * @author ： jsongo
 * @date ： 19-10-13 下午6:11
 * @desc : 可单独使用的WebView  需要fragment/activity实现 AjsWebViewHost 即可
 */
open class AJsWebView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    BridgeWebView(context, attrs, defStyleAttr) {

    companion object {

        /**
         * 加载纯文字的html模板
         */
        val infoHtml = """
            <html>
                <head>
                    <meta charset="UTF-8"/>
                    <meta name="viewport" content="width=device-width,initial-scale=1.0, minimum-scale=0.5,maximum-scale=2.0, user-scalable=yes">
                    <meta http-equiv="X-UA-Compatible" content="ie=edge"/>
                </head>
                <body>
                <h4>%s<h4>
                </body>
            </html>
        """.trimIndent()

    }

    /**
     * 加载的url
     */
    open var webPath = ""

    /**
     * 是否可以滑动  默认否   当卡片模式时，设置true，weview可滑动
     */
    var scrollable = false

    /**
     * 持有ajswebview的对象一般为AJsWebLoader  当 ajswebview作为view单独使用时，需要设置此对象为FragmentActivity/Fragment
     */
    open var ajsWebViewHost: AjsWebViewHost? = null

    /**
     * 加载进度监听
     */
    open var loadingProgressListener: LoadingProgressListener? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    /**
     * 初始化配置，并加载网页
     */
    open fun initLoad() {

        initWebSetting()

        isSaveEnabled = true
        keepScreenOn = true

        // 设置setWebChromeClient对象
        webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)
                loadingProgressListener?.onReceiveTitle(view, title)
            }

            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                loadingProgressListener?.onProgressChanged(view, progress)
            }

        }

        setOnPageFinishListener { view, url ->
            loadingProgressListener?.onLoadFinish(view, url)
        }

        setDownloadListener { paramStr1, paramStr2, paramStr3, paramStr4, paramLong ->
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse(paramStr1)
            ajsWebViewHost?.hostActivity?.startActivity(intent)
        }

        if (scrollable) {
            //解决滑动冲突问题
            setOnTouchListener { v, event ->
                requestDisallowInterceptTouchEvent(true)
                false
            }
        }

        load()
        //ridgeWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,aJsWebView.getHeight()));

        registerHandler()
    }

    /**
     * webviewclient 添加错误监听
     */
    override fun generateBridgeWebViewClient() = object : BridgeWebViewClient(this) {
        override fun onReceivedHttpError(
            p0: WebView?,
            p1: WebResourceRequest?,
            p2: WebResourceResponse?
        ) {
            super.onReceivedHttpError(p0, p1, p2)
            if (TextUtils.equals(webPath, p1?.url.toString())) {
                loadingProgressListener?.onReceiveError(p0, p1, p2?.statusCode, null)
            }
        }

        override fun onReceivedError(
            p0: WebView?,
            p1: WebResourceRequest?,
            p2: WebResourceError?
        ) {
            super.onReceivedError(p0, p1, p2)
            if (TextUtils.equals(webPath, p1?.url.toString())) {
                loadingProgressListener?.onReceiveError(p0, p1, p2?.errorCode, p2)
            }
        }
    }

    /**
     * 加载文本
     */
    open fun loadNormalInfo(info: String) = loadDataWithBaseURL(
        null,
        String.format(infoHtml, info),
        "text/html",
        "utf-8",
        null
    )

    /**
     * 加载页面
     */
    open fun load() {
        if (webPath.trim().startsWith(SRT_HTTP) || webPath.trim().startsWith(PRE_ANDROID_ASSET)) {
            loadUrl(webPath)
        } else {
            loadNormalInfo(webPath)
        }
        LogcatUtil.d("url ", webPath)
    }

    /**
     * 初始化websetting
     */
    open fun initWebSetting() {
        settings.apply {

            userAgentString += "@${this.javaClass}"
            if (Build.VERSION.SDK_INT >= 19) {
                cacheMode = WebSettings.LOAD_NO_CACHE//加载缓存否则网络
            }

            if (Build.VERSION.SDK_INT >= 19) {
                loadsImagesAutomatically = true//图片自动缩放 打开
            } else {
                loadsImagesAutomatically = false//图片自动缩放 关闭
            }

//           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//               aJsWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null)//软件解码
//           }
//           aJsWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null)//硬件解码

            //允许webview对文件的操作
            setAllowUniversalAccessFromFileURLs(true)
            allowFileAccess = true
            setAllowFileAccessFromFileURLs(true)

            allowContentAccess = true
            setAppCacheEnabled(true)
            //开启加载https
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW)
            }

            // setMediaPlaybackRequiresUserGesture(boolean require) //是否需要用户手势来播放Media，默认true

            // 设置支持javascript脚本
            javaScriptEnabled = true
            //setPluginState(WebSettings.PluginState.ON);

            // 设置可以支持缩放
            setSupportZoom(false)
            // 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false
            builtInZoomControls = false

            //隐藏缩放工具
            displayZoomControls = false
            // 扩大比例的缩放
            useWideViewPort = true

            //自适应屏幕
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            loadWithOverviewMode = true

            databaseEnabled = true//
            //保存密码
            savePassword = true
            //是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
            domStorageEnabled = true
        }
    }

    /**
     * 注册交互api
     */
    fun registerHandler() = ajsWebViewHost?.let { host ->
        InteractionRegisterCollector.interactionRegisterList.forEach {
            it.register(host, this@AJsWebView)
        }
    }


    interface LoadingProgressListener {

        fun onReceiveTitle(wv: WebView?, title: String?)

        fun onProgressChanged(wv: WebView?, newProgress: Int)

        fun onLoadFinish(wv: WebView?, url: String)

        fun onReceiveError(
            wv: WebView?,
            webResourceRequest: WebResourceRequest?,
            code: Int?,
            webResourceError: WebResourceError?
        )
    }
}