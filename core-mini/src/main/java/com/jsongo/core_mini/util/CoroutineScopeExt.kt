package com.jsongo.core_mini.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author ： jsongo
 * @date ： 2020/3/20 21:31
 * @desc : 协程的扩展方法
 */

/**
 * 运行在io线程
 */
fun CoroutineScope.launchIO(block: suspend CoroutineScope.() -> Unit) =
    launch(Dispatchers.IO, block = block)

suspend fun <T> withContextIO(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.IO, block = block)

/**
 * 运行在主线程
 */
fun CoroutineScope.launchMain(block: suspend CoroutineScope.() -> Unit) =
    launch(Dispatchers.Main, block = block)


suspend fun <T> withContextMain(block: suspend CoroutineScope.() -> T) =
    withContext(Dispatchers.Main, block = block)