package com.jsongo.mybasefrm.jsloader.interaction

import android.util.Log
import com.github.lzyzsd.jsbridge.CallBackFunction
import com.google.gson.reflect.TypeToken
import com.jsongo.mybasefrm.jsloader.AJsWebLoader
import com.jsongo.mybasefrm.jsloader.interaction.Util.gson
import com.jsongo.mybasefrm.jsloader.jsbridge.BridgeWebView

/**
 * @author  jsongo
 * @date 2019/6/18 21:38
 * @desc 已有的交互api
 */
object DefaultInteractionRegister {
    //交互api的包路径
    const val packageName = "com.jsongo.mybasefrm.jsloader.interaction"
    //api与类名映射
    val nameMapping = hashMapOf(
        Pair("topbar", "$packageName.Topbar"),
        Pair("common", "$packageName.Common"),
        Pair("toast", "$packageName.Toast")
    )
    //已有的api
    val defaultInteractionAPI = arrayListOf(
        "topbar.bgcolor",
        "topbar.hide",
        "topbar.title",
        "topbar.statusbar",
        "common.back",
        "common.messagedialog",
        "common.loading",
        "common.localpic",
        "toast.error",
        "toast.warning",
        "toast.info",
        "toast.normal",
        "toast.success"
    )

    //注册已有的api
    fun register(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView
    ) {
        defaultInteractionAPI.forEach {
            //获取类名全路径和方法名
            val split = it.split(".")
            if (split.size != 2) {
                return
            }
            val className = nameMapping[split[0]] ?: ""
            val methodName = split[1]

            if (className.isNotEmpty() && methodName.isNotEmpty()) {
                //注册api
                bridgeWebView.registerHandler(it) { data, function ->
                    Log.e("defaultactioncall", "method:$it,param_str:$data")
                    try {
                        val params = gson.fromJson<Map<String, String>>(
                            data,
                            object : TypeToken<Map<String, String>>() {}.type
                        )
                        val clazz = Class.forName(className)
                        val method = clazz.getDeclaredMethod(
                            split[1],
                            AJsWebLoader::class.java,
                            BridgeWebView::class.java,
                            Map::class.java,
                            CallBackFunction::class.java
                        )
                        method.isAccessible = true
                        method.invoke(null, jsWebLoader, bridgeWebView, params, function)
                    } catch (e: Exception) {
                        val map = HashMap<String, String>()
                        map["result"] = "0"
                        map["error"] = e.message ?: ""
                        val result = gson.toJson(map)
                        function.onCallBack(result)
                    }
                }
            }
        }
    }

}