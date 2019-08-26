package com.jsongo.ajs.interaction

import android.util.Log
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.google.gson.reflect.TypeToken
import com.jsongo.ajs.Util.gson
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.safframework.log.L

/**
 * @author  jsongo
 * @date 2019/6/18 21:38
 * @desc 已有的交互api
 */
object DefaultInteractionRegister {
    //交互api的包路径
    const val packageName = "com.jsongo.ajs.interaction"
    //api与类名映射
    val nameMapping = hashMapOf(
        Pair("cache", "$packageName.Cache"),
        Pair("common", "$packageName.Common"),
        Pair("loading", "$packageName.Loading"),
        Pair("smartrefresh", "$packageName.SmartRefresh"),
        Pair("toast", "$packageName.Toast"),
        Pair("topbar", "$packageName.Topbar")
    )
    //已有的api
    val defaultInteractionAPI = arrayListOf(
        "cache.put",
        "cache.get",

        "common.back",
        "common.messagedialog",
        "common.localpic",
        "common.showpic",
        "common.go",
        "common.load",

        "loading.show",
        "loading.hide",

        "smartrefresh.enableRefresh",
        "smartrefresh.enableLoadmore",
        "smartrefresh.color",
        "smartrefresh.header",
        "smartrefresh.footer",

        "toast.error",
        "toast.warning",
        "toast.info",
        "toast.normal",
        "toast.success",

        "topbar.bgcolor",
        "topbar.hide",
        "topbar.title",
        "topbar.statusbar"
    )

    //注册已有的api
    fun register(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView
    ) {
        val type = object : TypeToken<Map<String, String>>() {}.type
        defaultInteractionAPI.forEach {
            //获取类名全路径和方法名
            val split = it.split(".")
            if (split.size != 2) {
                return@forEach
            }
            val className = nameMapping[split[0]] ?: ""
            val methodName = split[1]

            if (className.isNotEmpty() && methodName.isNotEmpty()) {
                //注册api
                bridgeWebView.registerHandler(it) { data, function ->
                    Log.e("defaultactioncall", "method:$it,param_str:$data")
                    try {
                        val params = gson.fromJson<Map<String, String>>(
                            data, type
                        )
                        val clazz = Class.forName(className)
                        val method = clazz.getDeclaredMethod(
                            methodName,
                            AJsWebLoader::class.java,
                            BridgeWebView::class.java,
                            Map::class.java,
                            CallBackFunction::class.java
                        )
                        method.isAccessible = true
                        method.invoke(null, jsWebLoader, bridgeWebView, params, function)
                    } catch (e: Exception) {
                        L.e("exception occured", e.cause ?: e)
                        val map = HashMap<String, String>()
                        map["result"] = "0"
                        map["message"] = "exception occured"
                        map["error"] = e.cause?.message ?: ""
                        val result = gson.toJson(map)
                        function.onCallBack(result)
                    }
                }
            }
        }
    }

}