package com.jsongo.mybasefrm

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * author ： jsongo
 * createtime ： 2019/7/28 20:07
 * desc :
 */
class CoroutinesTest {

    @Test
    fun test1() {
        aC.start()
        println("Hello,") // main thread continues while coroutine is delayed
        Thread.sleep(2000L) // block main thread for 2 seconds to keep JVM alive
    }

    val aC = runBlocking {
        launch {
            // launch a new coroutine in background and continue
            delay(1000L) // non-blocking delay for 1 second (default time unit is ms)
            println("World!") // print after delay
        }
    }
}