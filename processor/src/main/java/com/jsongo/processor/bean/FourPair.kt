package com.jsongo.processor.bean

import java.io.Serializable

/**
 * @author ： jsongo
 * @date ： 19-9-20 上午12:10
 * @desc :
 */
data class FourPair<A, B, C, D>(
    var first: A,
    var second: B,
    var third: C,
    var fourth: D
) : Serializable