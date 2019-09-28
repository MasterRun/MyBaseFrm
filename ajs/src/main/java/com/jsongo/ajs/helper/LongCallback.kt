package com.jsongo.ajs.helper

/**
 * @author ： jsongo
 * @date ： 19-9-28 下午4:09
 * @desc :
 */
interface LongCallback<in T> {
    fun success(data: T?)

    fun failed(data: T?)

}