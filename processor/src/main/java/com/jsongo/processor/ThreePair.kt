package com.jsongo.processor

import java.io.Serializable

/**
 * @author ： jsongo
 * @date ： 19-9-20 上午12:10
 * @desc :
 */
data class ThreePair<out A, out B, out C>(
    public val first: A,
    public val second: B,
    public val third: C
) : Serializable