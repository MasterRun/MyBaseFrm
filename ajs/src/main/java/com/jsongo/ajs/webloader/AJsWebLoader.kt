package com.jsongo.ajs.webloader

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import com.jsongo.ajs.helper.InteractionRegisterCollector
import com.jsongo.ajs.helper.LongCallback
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.activity_ajs_webloader.*

/**
 * @author ： jsongo
 * @date ： 19-10-3 下午5:13
 * @desc : fragment 加载html
 */
class AJsWebLoader : BaseWebLoader() {

    companion object {

        const val SHOW_TOPBAR = "showTopBar"

        fun newInstance(url: String, showTopBar: Boolean = true) = AJsWebLoader().apply {
            webPath = url
            arguments = Bundle().apply {
                putBoolean(SHOW_TOPBAR, showTopBar)
            }

        }

    }

    override var containerIndex = 2

    /**
     * hostActivity 设置加载dialog之后不会使用emptyview
     */
    var loadingDialog: QMUITipDialog? = null

    /**
     * 长回调
     */
    val longCallbacks = SparseArray<LongCallback<Intent>>()

    override fun init() {
    }

    override fun initView(view: View) {
        super.initView(view)

        if (arguments?.getBoolean(SHOW_TOPBAR) == false) {
            topbar.visibility = View.GONE
        } else {
            topbar.setTitle("")
            //左上角直接退出页面
            topbar.backImageButton.setOnClickListener { view ->
                /*if (bridgeWebView.canGoBack()) {
                  bridgeWebView.goBack()
              } else {
                  finish()
              }*/
                activity?.finish()
            }
        }

        /*if (bridgeWebView.progress < 100) {
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
                bridgeWebView.reload()
                //显示进度
                pb_webview.visibility = View.VISIBLE
                pb_webview.progress = 0
                //显示加载
                if (loadingDialog != null) {
                    loadingDialog?.show()
                }
            }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //显示加载
        if (loadingDialog != null) {
            loadingDialog?.show()
        } else {
            inflateEmptyView()?.show(true, "加载中...", null, null, null)
        }
    }

    /**
     * 页面加载完成
     */
    override fun onLoadFinish(wv: WebView, url: String) {
        super.onLoadFinish(wv, url)
        if (loadingDialog != null) {
            loadingDialog?.dismiss()
        } else {
            inflateEmptyView()?.hide()
        }
    }

    /**
     * 用于注册java js交互
     */
    override fun registerHandler() {
        //注册交互api
        InteractionRegisterCollector.interactionRegisterList.forEach {
            it.register(this, bridgeWebView)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val longCallback = longCallbacks.get(requestCode)
        longCallback?.let {
            if (resultCode == Activity.RESULT_OK) {
                it.success(data)
            } else {
                it.failed(data)
            }
            longCallbacks.remove(requestCode)
        }
    }

    /**
     * 添加长回调
     */
    fun addLongCallback(requestCode: Int, longCallback: LongCallback<Intent>) {
        longCallbacks.put(requestCode, longCallback)
    }

    override fun onIPageDestory() {
        bridgeWebView.destroy()
        super.onIPageDestory()
    }

}
