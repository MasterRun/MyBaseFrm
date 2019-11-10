package com.jsongo.annotation.util

/**
 * @author ： jsongo
 * @date ： 19-9-20 上午8:45
 * @desc :
 */
object Util {
    /**
     * 通过全类名获取包名和类名
     */
    fun getPkgClazzName(rawClazzName: String): Pair<String, String> {
        val lastIndexOfDot = rawClazzName.lastIndexOf('.')
        //包名
        val pkgName = rawClazzName.subSequence(0, lastIndexOfDot)

        //类名
        val simpleName = rawClazzName.subSequence(lastIndexOfDot + 1, rawClazzName.length)

        return Pair(pkgName.toString(), simpleName.toString())
    }

}