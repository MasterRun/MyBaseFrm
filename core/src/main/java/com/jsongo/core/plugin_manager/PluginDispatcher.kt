package com.jsongo.core.plugin_manager

import android.text.TextUtils
import com.jsongo.core.util.CommonCallBack
import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

object PluginDispatcher {

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
        callback: CommonCallBack? = null
    ): PluginEvent.Invoke {
        val invoke = PluginEvent.Invoke(invokePath, params, callback)
        dispatch(invoke)
        return invoke
    }

    fun invoke(
        pluginName: String,
        method: String,
        params: Map<String, Any?>? = null,
        callback: CommonCallBack? = null
    ): PluginEvent.Invoke {
        val invoke = PluginEvent.Invoke(pluginName, method, params, callback)
        dispatch(invoke)
        return invoke
    }

    /**
     * 组件页面打开事件
     */
    fun route(
        invokePath: String,
        params: Map<String, Any?>? = null,
        callback: CommonCallBack? = null
    ): PluginEvent.Route {
        val route = PluginEvent.Route(invokePath, params, callback)
        dispatch(route)
        return route
    }

    fun route(
        pluginName: String,
        pageName: String,
        params: Map<String, Any?>? = null,
        callback: CommonCallBack? = null
    ): PluginEvent.Route {
        val route = PluginEvent.Route(pluginName, pageName, params, callback)
        dispatch(route)
        return route
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
