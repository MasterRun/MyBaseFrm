package com.jsongo.core.plugin

import com.jsongo.core.bean.DataWrapper
import com.jsongo.core.common.CommonCallBack

/**
 * @author ： jsongo
 * @date ： 2020/1/15 20:47
 * @desc :
 */
interface IPlugin {

    val name: String

    /**
     * 方法名以下划线开头表示一部调用使用回调接收结果，否则是同步调用，直接取返回值
     */
    fun invoke(
        methodName: String,
        params: Map<String, Any?>?,
        callback: CommonCallBack?
    ): DataWrapper<MutableMap<String, Any?>>

    fun route(
        pageName: String,
        params: Map<String, Any?>?,
        callback: CommonCallBack?
    ): DataWrapper<MutableMap<String, Any?>>
}