package com.jsongo.ajs.interaction

import com.jsongo.ajs.helper.AjsCallback
import com.jsongo.ajs.helper.AjsWebViewHost
import com.jsongo.ajs.widget.AJsWebView
import com.jsongo.core.db.CommonDbOpenHelper
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers

/**
 * author ： jsongo
 * createtime ： 19-8-25 下午11:35
 * desc : sqlite缓存
 */
object Cache {

    /**
     * 存值
     */
    @JvmStatic
    fun put(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val key = params["key"] ?: ""
        val value = params["value"] ?: ""
        if (key.isEmpty()) {
            callback.failure()
        } else {
            val disposable = Observable.just(Pair(key, value))
                .map {
                    CommonDbOpenHelper.setKeyValue(it.first, it.second)
                }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    callback.success()
                }
            ajsWebViewHost.compositeDisposable.add(disposable)
        }
    }

    /**
     * 取值
     */
    @JvmStatic
    fun get(
        ajsWebViewHost: AjsWebViewHost,
        aJsWebView: AJsWebView,
        params: Map<String, String>,
        callback: AjsCallback
    ) {
        val key = params["key"] ?: ""
        if (key.isEmpty()) {
            callback.failure()
        } else {
            val disposable = Observable.just(key)
                .map {
                    CommonDbOpenHelper.getValue(key) ?: ""
                }
                .subscribeOn(Schedulers.io())
                .subscribe {
                    callback.success(Pair("value", it))
                }
            ajsWebViewHost.compositeDisposable.add(disposable)
        }
    }
}