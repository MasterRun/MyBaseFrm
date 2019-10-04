package com.jsongo.ajs.interaction

import android.content.Intent
import com.google.gson.reflect.TypeToken
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.ConstValue
import com.jsongo.ajs.helper.LongCallback
import com.jsongo.ajs.helper.Util
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.ajs.webloader.AJsWebPage
import com.jsongo.ui.component.ImagePreview.ImgPreviewClick
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlin.random.Random

/**
 * @author  jsongo
 * @date 2019/6/20 12:56
 * @desc
 */
object Common {

    /**
     * 模拟返回键
     */
    @JvmStatic
    fun back(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = jsWebLoader.activity
        if (hostActivity == null) {
            callback.failure(message = "hostActivity is null")
            return
        }
        hostActivity.onBackPressed()
        callback.success()
    }

    /**
     * message dialog
     */
    @JvmStatic
    fun messagedialog(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val activity = jsWebLoader.activity
        if (activity == null) {
            callback.failure(message = "hostActivity is null")
            return
        }
        val qmuiDialogBuilder = QMUIDialog.MessageDialogBuilder(activity)
        val title = params["title"] ?: ""
        val message = params["message"] ?: ""
        if (title.isNotEmpty()) {
            qmuiDialogBuilder.setTitle(title)
        }
        if (message.isNotEmpty()) {
            qmuiDialogBuilder.setMessage(message)
        }

        val action1 = params["action1"] ?: ""
        if (action1.isNotEmpty()) {
            val action1mode = (params["action1mode"] ?: "0").toInt()
            val method1 = params["method1"] ?: ""
            val params1 = params["params1"] ?: ""
            val actionListener1 = QMUIDialogAction.ActionListener { dialog, index ->
                bridgeWebView.callHandler(method1, params1) { data ->

                }
                dialog?.dismiss()
            }
            if (action1mode == 1) {
                qmuiDialogBuilder.addAction(
                    0, action1, QMUIDialogAction.ACTION_PROP_NEGATIVE, actionListener1
                )
            } else {
                qmuiDialogBuilder.addAction(action1, actionListener1)
            }
        }

        val action2 = params["action2"] ?: ""
        if (action2.isNotEmpty()) {
            val action2mode = (params["action2mode"] ?: "0").toInt()
            val method2 = params["method2"] ?: ""
            val params2 = params["params2"] ?: ""
            val actionListener2 = QMUIDialogAction.ActionListener { dialog, index ->
                bridgeWebView.callHandler(method2, params2) { data ->

                }
                dialog?.dismiss()
            }
            if (action2mode == 1) {
                qmuiDialogBuilder.addAction(
                    0, action2, QMUIDialogAction.ACTION_PROP_NEGATIVE, actionListener2
                )
            } else {
                qmuiDialogBuilder.addAction(action2, actionListener2)
            }
        }

        val qmuiDialog = qmuiDialogBuilder.create()
        val cancelable = (params["cancelable"] ?: "true").toBoolean()
        qmuiDialog.setCancelable(cancelable)
        qmuiDialog.show()

        callback.success()
    }

    /**
     * 获取本地图片资源路径,添加本地文件路径标识，webloader可以拦截的标识
     */
    @JvmStatic
    fun localpic(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val path = "${ConstValue.LocalPicPrefix}${params["path"]}"
        callback.success(Pair("path", path))
    }

    /**
     * 图片预览
     */
    @JvmStatic
    fun showpic(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = jsWebLoader.activity
        if (hostActivity == null) {
            callback.failure(message = "hostActivity is null")
            return
        }
        val urls = params["urls"].toString()
        val urlList =
            Util.gson.fromJson<List<String>>(urls, object : TypeToken<List<String>>() {}.type)
        val index = (params["index"] ?: "0").toInt()

        ImgPreviewClick(hostActivity, index, urlList).start()

        callback.success()
    }

    /**
     * 加载网页
     */
    @JvmStatic
    fun load(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val url = params["url"].toString()
        AJsWebPage.load(url)
        callback.success()
    }

    /**
     * 跳转原生页面
     */
    @JvmStatic
    fun go(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = jsWebLoader.activity
        if (hostActivity == null) {
            callback.failure(message = "hostActivity is null")
            return
        }
        val activity = params["activity"].toString()
        val activityClazz = Class.forName(activity)
        val intent = Intent(hostActivity, activityClazz)
        jsWebLoader.startActivity(intent)
        callback.success()
    }

    /**
     * 打开二维码条形码扫描页面
     */
    @JvmStatic
    fun scan(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val hostActivity = jsWebLoader.activity
        if (hostActivity == null) {
            callback.failure(message = "hostActivity is null")
            return
        }
        var requestCode = (params["requestCode"] ?: "0").toInt()
        if (requestCode == 0) {
            requestCode = Random.nextInt()
        }
        val intent = Intent(hostActivity, CaptureActivity::class.java)
        jsWebLoader.startActivityForResult(intent, requestCode)

        jsWebLoader.addLongCallback(requestCode, object : LongCallback<Intent> {
            override fun success(data: Intent?) {
                if (data != null) {
                    val str = data.getStringExtra(Constant.CODED_CONTENT)
                    callback.success(Pair("data", str))
                } else {
                    failed(data)
                }
            }

            override fun failed(data: Intent?) {
                callback.failure()
            }
        })
    }

}