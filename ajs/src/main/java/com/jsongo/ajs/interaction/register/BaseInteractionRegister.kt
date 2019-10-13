package com.jsongo.ajs.interaction.register

import com.google.gson.reflect.TypeToken
import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.helper.Util
import com.jsongo.ajs.widget.AJsWebView
import com.safframework.log.L

/**
 * author ： jsongo
 * createtime ： 19-8-27 下午8:44
 * desc : 交互api的基类
 */
abstract class BaseInteractionRegister {
    /**
     * api 映射
     * eg:  "cache.put", "com.jsongo.ajs.interaction.Cache.put"
     */
    abstract val interactionAPI: Map<String, String>

    val type = object : TypeToken<Map<String, String>>() {}.type

    //注册api
    fun register(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView
    ) {

        interactionAPI.forEach {
            //获取类名全路径和方法名
            val value = it.value
            val lastIndex = value.lastIndexOf(".")
            val className = value.substring(0, lastIndex)
            val methodName = value.substring(lastIndex + 1)

            if (className.isNotEmpty() && methodName.isNotEmpty()) {
                //注册api
                L.d("registerApi", "registerName:${it.key}")
                registerApi(ajsWebViewHost, aJsWebView, it.key, className, methodName)
            }
        }
    }

    private fun registerApi(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        registerName: String,
        className: String,
        methodName: String
    ) {
        aJsWebView.registerHandler(registerName) { data, function ->
            val ajsCallback = AjsCallback(function)
            L.d("invokeAjsApi", "registerName:$registerName, param_str:$data")
            try {
                val params = Util.gson.fromJson<Map<String, String>>(
                    data, type
                )
                val clazz = Class.forName(className)
                val method = clazz.getDeclaredMethod(
                    methodName,
                    AjsWebViewHost::class.java,
                    AJsWebView::class.java,
                    Map::class.java,
                    AjsCallback::class.java
                )
                method.isAccessible = true
                method.invoke(null, ajsWebViewHost, aJsWebView, params, ajsCallback)
            } catch (e: Exception) {
                L.e("exception occured", e.cause ?: e)
                ajsCallback.failure(e)
            }
        }
    }
}

