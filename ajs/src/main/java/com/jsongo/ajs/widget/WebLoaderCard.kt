package com.jsongo.ajs.widget

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.helper.AjsWebViewHost
import com.qmuiteam.qmui.widget.QMUIEmptyView
import com.tencent.smtt.export.external.interfaces.WebResourceError
import com.tencent.smtt.export.external.interfaces.WebResourceRequest
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.card_web_loader.view.*

/**
 * @author ： jsongo
 * @date ： 19-10-13 下午5:03
 * @desc : webloader的card
 */
open class WebLoaderCard(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    CardView(context, attrs, defStyleAttr) {

    open var scrollable = false
        set(value) {
            field = value
            aJsWebView?.scrollable = value
        }

    open var url = ""
        set(value) {
            field = value
            aJsWebView?.webPath = url
        }

    open var ajsWebViewHost: AjsWebViewHost? = null
        set(value) {
            field = value
            aJsWebView?.ajsWebViewHost = value
        }

    /**
     * 适用emptyview的加载
     */
    open var showEmptyViewLoading = true
    /**
     * 显示加载进入的progressbar
     */
    open var showLoadingProgress = true
    /**
     * 错误时使用emptyview
     */
    open var showEmptyViewOnError = true

    val aJsWebView: AJsWebView
    val emptyView: QMUIEmptyView
    val pbWebview: ProgressBar

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(
        context,
        attrs,
        R.attr.cardViewStyle
    )

    init {
        if (attrs != null) {
            val typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.WebLoaderCard)
            scrollable =
                typedArray.getBoolean(R.styleable.WebLoaderCard_scrollable, false)
            url = typedArray.getString(R.styleable.WebLoaderCard_url) ?: ""
            showEmptyViewLoading =
                typedArray.getBoolean(R.styleable.WebLoaderCard_showEmptyViewLoading, true)
            showLoadingProgress =
                typedArray.getBoolean(R.styleable.WebLoaderCard_showLoadingProgress, true)
            showEmptyViewOnError =
                typedArray.getBoolean(R.styleable.WebLoaderCard_showEmptyViewOnError, true)
            typedArray.recycle()
        }

        LayoutInflater.from(context).inflate(R.layout.card_web_loader, this)
        aJsWebView = ajs_webview
        aJsWebView.webPath = url
        aJsWebView.scrollable = scrollable

        emptyView = findViewById(R.id.empty_view)
        pbWebview = pb_webview

        //设置加载进度最大值
        pbWebview.max = 100

        emptyView.setBackgroundColor(Color.WHITE)

        if (showEmptyViewLoading) {
            pbWebview.visibility = View.VISIBLE
        } else {
            pbWebview.visibility = View.GONE
        }
        if (showEmptyViewLoading.not()) {
            emptyView.visibility = View.GONE
        }
    }

    open fun initLoad() {
        if (showEmptyViewLoading) {
            emptyView.show(true, AJs.context.getString(R.string.ajs_loading), null, null, null)
        }
        aJsWebView.loadingProgressListener = object : AJsWebView.LoadingProgressListener {
            override fun onReceiveTitle(wv: WebView?, title: String?) {
            }

            override fun onProgressChanged(wv: WebView?, newProgress: Int) {
                if (showLoadingProgress) {
                    if (pbWebview.visibility != View.VISIBLE) {
                        pbWebview.visibility = View.VISIBLE
                    }
                    pbWebview.progress = newProgress
                }
            }

            override fun onLoadFinish(wv: WebView?, url: String) {
                if (showLoadingProgress) {
                    pbWebview.visibility = View.GONE
                }
                if (showEmptyViewLoading) {
                    emptyView.hide()
                }
            }

            override fun onReceiveError(
                wv: WebView?,
                webResourceRequest: WebResourceRequest?,
                code: Int?,
                webResourceError: WebResourceError?
            ) {
                if (showLoadingProgress) {
                    pbWebview.visibility = View.GONE
                }
                if (showEmptyViewOnError) {
                    emptyView.show(
                        false,
                        AJs.context.getString(R.string.ajs_load_error),
                        "url:${aJsWebView.webPath}\n\n错误码:$code" + (if (webResourceError == null) "" else "\n错误信息:${webResourceError.description}"),
                        AJs.context.getString(R.string.ajs_reload)
                    ) {
                        reload()
                    }
                } else {
                    emptyView.hide()
                }
            }
        }
        aJsWebView.initLoad()
    }

    open fun load(url: String) {
        if (showEmptyViewLoading) {
            emptyView.show(true, AJs.context.getString(R.string.ajs_loading), null, null, null)
        }
        this.url = url
        aJsWebView.load()
    }

    open fun reload() {
        if (showEmptyViewLoading) {
            emptyView.show(true, AJs.context.getString(R.string.ajs_loading), null, null, null)
        } else {
            emptyView.hide()
        }
        aJsWebView.reload()
    }

    open fun destroy() {
        aJsWebView.destroy()
    }
}