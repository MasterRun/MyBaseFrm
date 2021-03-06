package com.jsongo.core_mini.common

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers

/**
 * @author jsongo
 * @date 2019/3/10 23:30
 */
object RxBus {

    private val mBus: FlowableProcessor<BusEvent<*>> =
        PublishProcessor.create<BusEvent<*>>().toSerialized()

    fun post(busEvent: BusEvent<*>) {
        mBus.onNext(busEvent)
    }

    fun toFlowable(): Flowable<BusEvent<*>> {
        return mBus.ofType(BusEvent::class.java).subscribeOn(Schedulers.io())
    }

    fun hasSubscribers(): Boolean {
        return mBus.hasSubscribers()
    }

}


/**
 * @author  jsongo
 * @date 2019/3/29 16:19
 * @desc rxbus event
 */
data class BusEvent<T>(
    var code: Int = -1,
    var message: String = "",
    var data: T? = null
)