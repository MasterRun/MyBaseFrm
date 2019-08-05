package com.jsongo.ajs.interaction

import android.content.Intent
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.google.gson.reflect.TypeToken
import com.jsongo.ajs.ConstValue
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.ajs.webloader.DefaultWebLoader
import com.jsongo.ui.component.ImagePreview.ImgPreviewClick
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction

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
        function: CallBackFunction
    ) {
        jsWebLoader.onBackPressed()
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * message dialog
     */
    @JvmStatic
    fun messagedialog(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val qmuiDialogBuilder = QMUIDialog.MessageDialogBuilder(jsWebLoader)
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

        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * 获取本地图片资源路径,添加本地文件路径标识，webloader可以拦截的标识
     */
    @JvmStatic
    fun localpic(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val path = "${ConstValue.LocalPicPrefix}${params["path"]}"
        val map = hashMapOf(
            Pair("result", "1"),
            Pair("path", path)
        )
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * 图片预览
     */
    @JvmStatic
    fun showpic(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val urls = params["urls"].toString()
        val urlList =
            Util.gson.fromJson<List<String>>(urls, object : TypeToken<List<String>>() {}.type)
        val index = (params["index"] ?: "0").toInt()

        ImgPreviewClick(jsWebLoader, index, urlList).start()

        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * 加载网页
     */
    @JvmStatic
    fun load(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val url = params["url"].toString()
        DefaultWebLoader.load(url)
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

    /**
     * 跳转原生页面
     */
    @JvmStatic
    fun go(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        function: CallBackFunction
    ) {
        val activity = params["activity"].toString()
        val activityClazz = Class.forName(activity)
        val intent = Intent(jsWebLoader, activityClazz)
        jsWebLoader.startActivity(intent)
        val map = hashMapOf(Pair("result", "1"))
        val result = Util.gson.toJson(map)
        function.onCallBack(result)
    }

}