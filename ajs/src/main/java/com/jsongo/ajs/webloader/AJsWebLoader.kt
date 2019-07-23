package com.jsongo.ajs.webloader

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.jsongo.ajs.R
import com.jsongo.ajs.interaction.DefaultInteractionRegister
import com.jsongo.core.mvp.base.BaseActivity
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.vondear.rxtool.RxKeyboardTool
import kotlinx.android.synthetic.main.activity_ajs_webloader.*

/**
 * @author jsongo
 * @date 2019/5/9 14:48
 * @desc modify from RxUI activitywebloader  mixin jsbridge replace by tencent x5 core
 */
abstract class AJsWebLoader : BaseActivity() {

    protected var webPath = ""

    override var mainLayoutId = R.layout.activity_ajs_webloader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        init()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        initView()// 初始化控件 - findViewById之类的操作
        initData()// 初始化控件的数据及监听事件
    }

    protected open fun init() {
    }

    protected fun initView() {
        //默认禁用侧滑返回
        setSwipeBackEnable(false)

        topbar.setTitle("")
        //左上角直接退出页面
        topbar.backImageButton.setOnClickListener { view ->
            /*if (bridgeWebView.canGoBack()) {
                  bridgeWebView.goBack()
              } else {
                  finish()
              }*/
            finish()
        }

        //隐藏输入法
        RxKeyboardTool.hideSoftInput(this)
        //可取消dialog
        loadingDialog.setCancelable(true)
        loadingDialog.show()
        /*if (bridgeWebView.progress < 100) {
            //显示加载中
        }else{
            loadingDialog?.dismiss()
        }*/

        //启用下拉刷新，禁用上拉加载更多
        smartRefreshLayout.isEnabled = true
        smartRefreshLayout
            .setEnableRefresh(true)
            .setEnableLoadMore(false)
            //下拉重新加载
            .setOnRefreshListener {
                bridgeWebView.reload()
                //显示进度
                pb_webview.visibility = View.VISIBLE
                pb_webview.progress = 0
                //显示加载dialog
                loadingDialog.show()
            }
    }

    protected fun initData() {
        pb_webview.max = 100//设置加载进度最大值
        val webSettings = bridgeWebView.settings
        webSettings.userAgentString = webSettings.userAgentString + "AJsWebLoader"
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

        webSettings.javaScriptEnabled = true // 设置支持javascript脚本
        //        webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setSupportZoom(false)// 设置可以支持缩放
        webSettings.builtInZoomControls =
            false// 设置出现缩放工具 是否使用WebView内置的缩放组件，由浮动在窗口上的缩放控制和手势缩放控制组成，默认false

        webSettings.displayZoomControls = false//隐藏缩放工具
        webSettings.useWideViewPort = true// 扩大比例的缩放

        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN//自适应屏幕
        webSettings.loadWithOverviewMode = true

        webSettings.databaseEnabled = true//
        webSettings.savePassword = true//保存密码
        webSettings.domStorageEnabled =
            true//是否开启本地DOM存储  鉴于它的安全特性（任何人都能读取到它，尽管有相应的限制，将敏感数据存储在这里依然不是明智之举），Android 默认是关闭该功能的。
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
            //页面加载完成后 隐藏加载进度和加载dialog
            pb_webview.visibility = View.GONE
            loadingDialog.dismiss()
            smartRefreshLayout.finishRefresh()
        }

        bridgeWebView.setDownloadListener { paramStr1, paramStr2, paramStr3, paramStr4, paramLong ->
            val intent = Intent()
            intent.action = "android.intent.action.VIEW"
            intent.data = Uri.parse(paramStr1)
            startActivity(intent)
        }

        bridgeWebView.loadUrl(webPath)
        Log.v("url ", webPath)
        //        bridgeWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,bridgeWebView.getHeight()));

        registerHandler()
    }

    /**
     * 用于注册java js交互
     */
    protected fun registerHandler() {
        //注册已有的交互api
        DefaultInteractionRegister.register(this, bridgeWebView)
    }

    override fun onSaveInstanceState(paramBundle: Bundle) {
        super.onSaveInstanceState(paramBundle)
        paramBundle.putString("url", bridgeWebView.url)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        try {
            super.onConfigurationChanged(newConfig)
            if (this.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                Log.v("Himi", "onConfigurationChanged_ORIENTATION_LANDSCAPE")
            } else if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Log.v("Himi", "onConfigurationChanged_ORIENTATION_PORTRAIT")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {

        if (bridgeWebView.canGoBack()) {
            bridgeWebView.goBack()
        } else {
            super.onBackPressed()
        }
    }

}
