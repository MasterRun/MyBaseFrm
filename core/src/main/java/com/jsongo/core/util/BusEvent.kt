package com.jsongo.core.util

/**
 * @author  jsongo
 * @date 2019/3/29 16:19
 * @desc rxbus event
 */
class BusEvent(
    var code: Int = 0,
    var message: String = "",
    var data: Any? = null
) {
    companion object {


    }
}