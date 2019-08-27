package com.jsongo.ajs.interaction.register

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.Util
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.safframework.log.L

/**
 * author ： jsongo
 * createtime ： 19-8-27 下午8:44
 * desc : 交互api的基类
 */
abstract class BaseInteractionRegister {
    /**
     * api 与 类名映射
     */
    abstract val nameMapping: Map<String, String>
    /**
     * api 集合
     */
    abstract val interactionAPI: List<String>

    val type = object : TypeToken<Map<String, String>>() {}.type

    //注册api
    fun register(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView
    ) {
        interactionAPI.forEach {
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
                    val ajsCallback = AjsCallback(function)
                    Log.e("defaultactioncall", "method:$it,param_str:$data")
                    try {
                        val params = Util.gson.fromJson<Map<String, String>>(
                            data, type
                        )
                        val clazz = Class.forName(className)
                        val method = clazz.getDeclaredMethod(
                            methodName,
                            AJsWebLoader::class.java,
                            BridgeWebView::class.java,
                            Map::class.java,
                            AjsCallback::class.java
                        )
                        method.isAccessible = true
                        method.invoke(null, jsWebLoader, bridgeWebView, params, ajsCallback)
                    } catch (e: Exception) {
                        L.e("exception occured", e.cause ?: e)
                        ajsCallback.failure(e)
                    }
                }
            }
        }
    }
}

