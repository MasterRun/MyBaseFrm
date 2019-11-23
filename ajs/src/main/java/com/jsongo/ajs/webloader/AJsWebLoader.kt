package com.jsongo.ajs.webloader

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.jsongo.ajs.AJs
import com.jsongo.ajs.R
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ui.util.addStatusBarHeightPadding
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.activity_ajs_webloader.*

/**
 * @author ： jsongo
 * @date ： 19-10-3 下午5:13
 * @desc : fragment 加载html
 */
class AJsWebLoader : BaseWebLoader(), AjsWebViewHost {

    override val hostActivity: FragmentActivity?
        get() = activity
    override val hostFragment: Fragment?
        get() = this

    companion object {

        /**
         * @param url 加载的utl
         * @param showTopBar 是否显示topbar  默认是
         * @param scrollable 是否可以滑动  默认否  当卡片模式时，设置true，weview可滑动
         */
        @JvmStatic
        fun newInstance(
            url: String,
            showTopBar: Boolean = true,
            scrollable: Boolean = false,
            fixHeight: Boolean = true,
            bgColor: Int = Color.TRANSPARENT
        ) =
            AJsWebLoader().apply {
                webPath = url
                this.showTopBar = showTopBar
                this.scrollable = scrollable
                this.fixHeight = fixHeight
                this.bgColor = bgColor
            }

    }

    override var containerIndex = 2

    /**
     * 是否显示topbar
     */
    var showTopBar = true
    /**
     * 是否可以滑动  默认否   当卡片模式时，设置true，weview可滑动
     */
    var scrollable = false

    /**
     * 修复高度
     */
    var fixHeight = true

    /**
     * 背景色
     */
    var bgColor: Int = Color.TRANSPARENT

    /**
     * hostActivity 设置加载dialog之后不会使用emptyview
     */
    var loadingDialog: QMUITipDialog? = null

    override fun init() {
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun initView(view: View) {
        super.initView(view)

        aJsWebView.scrollable = scrollable

        //是否显示topbar
        if (!showTopBar) {
            topbar.visibility = View.GONE
        } else {
            topbar.setTitle("")
            //左上角直接退出页面
            topbar.backImageButton.setOnClickListener { view ->
                /*if (aJsWebView.canGoBack()) {
                  aJsWebView.goBack()
              } else {
                  finish()
              }*/
                activity?.finish()
            }
        }

        /*if (aJsWebView.progress < 100) {
            //显示加载中
        } else {
            loadingDialog?.dismiss()
        }*/

        //启用下拉刷新，禁用上拉加载更多
        smartRefreshLayout.isEnabled = true
        smartRefreshLayout
            .setEnableRefresh(true)
            .setEnableLoadMore(false)
            //下拉重新加载
            .setOnRefreshListener {
                aJsWebView.reload()
                //显示进度
                pb_webview.visibility = View.VISIBLE
                pb_webview.progress = 0
                //显示加载
                if (loadingDialog != null) {
                    loadingDialog?.show()
                }
            }

        //修正高度
        if (fixHeight) {
            if (showTopBar) {
                topbar.post {
                    topbar.addStatusBarHeightPadding()
                }
            } else {
                //设置底部颜色，在添加padding
                rlLayoutRoot.setBackgroundColor(bgColor)
                flAjsWebRoot.addStatusBarHeightPadding()
            }
        }
        //设置背景色
        flAjsWebRoot.setBackgroundColor(bgColor)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //显示加载
        if (loadingDialog != null) {
            loadingDialog?.show()
        } else {
            inflateEmptyView()?.show(
                true,
                AJs.context.getString(R.string.ajs_loading),
                null,
                null,
                null
            )
        }
    }

    /**
     * 重新加载
     */
    override fun reload() {
        super.reload()
        if (loadingDialog != null) {
            loadingDialog?.show()
        } else {
            inflateEmptyView()?.show(
                true,
                AJs.context.getString(R.string.ajs_loading),
                null,
                null,
                null
            )
        }
    }

    /**
     * 页面加载完成
     */
    override fun onLoadFinish(wv: WebView?, url: String) {
        super.onLoadFinish(wv, url)
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
        } else {
            inflateEmptyView()?.hide()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onAjsLongCallBack(requestCode, resultCode, data)
    }

    override fun onIPageDestroy() {
        aJsWebView.destroy()
        super.onIPageDestroy()
    }

}
