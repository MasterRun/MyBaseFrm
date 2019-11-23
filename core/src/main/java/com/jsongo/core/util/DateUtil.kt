package com.jsongo.core.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * @author ： jsongo
 * @date ： 2019/11/23 10:44
 * @desc :
 */
object DateUtil {

    /**
     * 获取当前时间的str
     */
    fun getCurrentTimeStr(formatStr: String = DATE_STR_FORMAT_STR) =
        SimpleDateFormat(formatStr, Locale.CHINA).format(Date())!!

}