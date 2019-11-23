package com.jsongo.core.util

import java.util.regex.Pattern

/**
 * @author ： jsongo
 * @date ： 2019/11/23 11:22
 * @desc :
 */
object RegUtil {

    /**
     * string是否匹配regex正则表达式字符串
     *
     * @param regex  正则表达式字符串
     * @param string 要匹配的字符串
     * @return `true`: 匹配<br></br>`false`: 不匹配
     */
    fun isMatch(regex: String?, string: String?): Boolean {
        return !string.isNullOrEmpty() && Pattern.matches(regex, string)
    }

}