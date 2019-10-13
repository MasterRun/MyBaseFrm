package com.jsongo.ajs.widget

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import com.jsongo.ajs.R
import com.jsongo.ajs.helper.AjsWebViewHost
import kotlinx.android.synthetic.main.card_web_loader.view.*

/**
 * @author ： jsongo
 * @date ： 19-10-13 下午5:03
 * @desc : webloader的card
 */
class WebLoaderCard(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    CardView(context, attrs, defStyleAttr) {

    var scrollable = false
        set(value) {
            field = value
            aJsWebView?.scrollable = value
        }

    var url = ""
        set(value) {
            field = value
            aJsWebView?.webPath = url
        }

    var ajsWebViewHost: AjsWebViewHost? = null
        set(value) {
            field = value
            aJsWebView?.ajsWebViewHost = value
        }
    var aJsWebView: AJsWebView

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
            typedArray.recycle()
        }

        LayoutInflater.from(context).inflate(R.layout.card_web_loader, this)
        aJsWebView = ajs_webview
        aJsWebView.webPath = url
        aJsWebView.scrollable = scrollable
    }

    fun initLoad() {
        aJsWebView.initLoad()
    }

    fun load(url: String) {
        this.url = url
        aJsWebView.load()
    }

    fun reload() {
        aJsWebView.reload()
    }
}