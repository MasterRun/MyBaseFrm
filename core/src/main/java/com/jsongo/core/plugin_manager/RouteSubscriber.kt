package com.jsongo.core.plugin_manager

import io.reactivex.disposables.Disposable
import io.reactivex.exceptions.CompositeException
import io.reactivex.exceptions.Exceptions
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.plugins.RxJavaPlugins
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

/**
 * @author ： jsongo
 * @date ： 2020/1/3 23:02
 * @desc :
 */
class RouteSubscriber(
    private val onNext: Consumer<in PluginEvent.Route>?,
    private val onError: Consumer<in Throwable?>? = null,
    private val onComplete: Action? = null
) : Subscriber<PluginEvent.Route>, Disposable {
    var pluginEvent: PluginEvent.Route? = null
    var subscription: Subscription? = null

    var disposabled = false

    override fun onSubscribe(s: Subscription) {
        subscription = s
        s.request(Long.MAX_VALUE)
    }

    override fun onNext(t: PluginEvent.Route) {
        if (!disposabled) {
            try {
                pluginEvent = t
                pluginEvent?.disposable = this
                onNext?.accept(t)
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                onError(e)
            }
        }
    }

    override fun onError(t: Throwable) {
        if (!disposabled) {
            disposabled = true
            try {
                if (onError == null) {
                    t.printStackTrace()
                    pluginEvent?.callback?.failed(-1, t.message ?: "", t)
                } else {
                    onError.accept(t)
                }
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(CompositeException(t, e))
            }
        } else {
            RxJavaPlugins.onError(t)
        }
    }

    override fun onComplete() {
        if (!disposabled) {
            disposabled = true
            try {
                onComplete?.run()
            } catch (e: Throwable) {
                Exceptions.throwIfFatal(e)
                RxJavaPlugins.onError(e)
            }
        }
    }

    override fun dispose() {
        disposabled = true
        subscription?.cancel()
    }

    override fun isDisposed(): Boolean {
        return disposabled
    }
}