package com.jsongo.core.rxplugin

import android.text.TextUtils
import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

/**
 * @author ： jsongo
 * @date ： 2019/12/24 16:55
 * @desc :
 */

object RxPluginDispatcher {

    private val mPlugin: FlowableProcessor<PluginEvent> =
        PublishProcessor.create<PluginEvent>().toSerialized()

    // region 组件事件分发
    fun dispatch(pluginEvent: PluginEvent) {
        mPlugin.onNext(pluginEvent)
    }

    /**
     * 组件方法调用事件
     */
    fun invoke(
        invokePath: String,
        params: Map<String, Any?>? = null,
        callback: PluginEvent.EventCallback? = null
    ) {
        dispatch(PluginEvent.Invoke(invokePath, params, callback))
    }

    fun invoke(
        pluginName: String,
        method: String,
        params: Map<String, Any?>? = null,
        callback: PluginEvent.EventCallback? = null
    ) {
        dispatch(PluginEvent.Invoke(pluginName, method, params, callback))
    }

    /**
     * 组件页面打开事件
     */
    fun route(
        invokePath: String,
        params: Map<String, Any?>? = null,
        callback: PluginEvent.EventCallback? = null
    ) {
        dispatch(PluginEvent.Route(invokePath, params, callback))
    }

    fun route(
        pluginName: String,
        pageName: String,
        params: Map<String, Any?>? = null,
        callback: PluginEvent.EventCallback? = null
    ) {
        dispatch(PluginEvent.Route(pluginName, pageName, params, callback))
    }
    // endregion

    // region 组件事件监听
    fun ofPluginEvent(): Flowable<PluginEvent> {
        return mPlugin.ofType(PluginEvent::class.java)
    }

    fun ofInvoke(pluginName: String = ""): Flowable<PluginEvent.Invoke> {
        var ofType = mPlugin.ofType(PluginEvent.Invoke::class.java)
        if (!TextUtils.isEmpty(pluginName)) {
            ofType = ofType.filter {
                TextUtils.equals(pluginName, it.pluginName)
            }
        }
        return ofType
    }

    fun ofRoute(pluginName: String = ""): Flowable<PluginEvent.Route> {
        var ofType = mPlugin.ofType(PluginEvent.Route::class.java)
        if (!TextUtils.isEmpty(pluginName)) {
            ofType = ofType.filter {
                TextUtils.equals(pluginName, it.pluginName)
            }
        }
        return ofType
    }
    // endregion

    fun hasSubscribers(): Boolean {
        return mPlugin.hasSubscribers()
    }

}

/**
 * 组件事件
 */
abstract class PluginEvent(
    val params: Map<String, Any?>?,
    val callback: EventCallback?
) {

    /**
     * 组件方法调用事件对象
     */
    class Invoke(invokePath: String, params: Map<String, Any?>?, callback: EventCallback?) :
        PluginEvent(params, callback) {
        constructor(
            pluginName: String,
            method: String,
            params: Map<String, Any?>?,
            callback: EventCallback?
        ) : this("$pluginName.$method", params, callback)

        val pluginName: String
        val method: String

        init {
            val split = invokePath.split(".")
            pluginName = split[0]
            method = split[1]
        }
    }

    /**
     * 组件页面打开事件对象
     */
    class Route(invokePath: String, params: Map<String, Any?>?, callback: EventCallback?) :
        PluginEvent(params, callback) {
        constructor(
            pluginName: String,
            pageName: String,
            params: Map<String, Any?>?,
            callback: EventCallback?
        ) : this("/$pluginName/$pageName", params, callback)

        val pluginName: String
        val pageName: String

        init {
            val split = invokePath.split("/")
            pluginName = split[1]
            pageName = split[2]
        }
    }

    /**
     * 回调事件
     */
    interface EventCallback {
        fun success(data: Map<String, Any?>?)
        fun failed(code: Int, msg: String, throwable: Throwable?)
    }
}
