package com.jsongo.core.plugin

import com.jsongo.core.bean.DataWrapper

/**
 * @author ： jsongo
 * @date ： 2020/1/15 20:47
 * @desc :
 */
interface IPlugin {

    val name: String

    fun invoke(methodName: String, params: Map<String, Any?>?): DataWrapper<MutableMap<String, Any?>>

    fun route(pageName: String, params: Map<String, Any?>?): DataWrapper<MutableMap<String, Any?>>
}