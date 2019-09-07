package com.jsongo.mybasefrm.ajsapi

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.interaction.register.BaseInteractionRegister
import com.jsongo.ajs.jsbridge.BridgeWebView
import com.jsongo.ajs.webloader.AJsWebLoader
import com.jsongo.annotation.AjsApi
import com.vondear.rxtool.view.RxToast

/**
 * author ： jsongo
 * createtime ： 19-9-4 下午11:25
 * desc :
 */
@AjsApi
object TestApi1 : BaseInteractionRegister() {

    override val nameMapping: Map<String, String> =
        hashMapOf(Pair("custom1", MyTestApi1::class.java.name))

    override val interactionAPI: List<String> = listOf("custom1.toast")
}

object MyTestApi1 {
    @JvmStatic
    fun toast(
        jsWebLoader: AJsWebLoader,
        bridgeWebView: BridgeWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val text = params["text"].toString()
        if (text.isEmpty()) {
            callback.failure()
            return
        }
        RxToast.error(text)
        callback.success()
    }
}