package com.jsongo.core.util

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

/**
 * @author jsongo
 * @date 2019/3/10 23:30
 */
object RxBus {

    private val mBus: FlowableProcessor<BusEvent> =
        PublishProcessor.create<BusEvent>().toSerialized()

    fun post(busEvent: BusEvent) {
        mBus.onNext(busEvent)
    }

    fun toFlowable(): Flowable<BusEvent> {
        return mBus.ofType(BusEvent::class.java)
    }

    fun hasSubscribers(): Boolean {
        return mBus.hasSubscribers()
    }

}