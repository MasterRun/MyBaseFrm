package com.jsongo.processor.bean

import java.io.Serializable

/**
 * @author ： jsongo
 * @date ： 19-9-20 上午12:10
 * @desc :
 */
data class ThreePair<out A, out B, out C>(
    val first: A,
    val second: B,
    val third: C
) : Serializable