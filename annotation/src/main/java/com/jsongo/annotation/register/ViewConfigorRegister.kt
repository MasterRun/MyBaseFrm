package com.jsongo.annotation.register

import com.jsongo.annotation.configor.Configor

/**
 * @author ： jsongo
 * @date ： 19-9-19 下午11:25
 * @desc :
 */
object ViewConfigorRegister {
    fun config(any: Any) {
        val name = any.javaClass.name
        val (genPkgName, genClazzName) = getGenPkgName(name)
        try {
            val configorClazz = Class.forName("${genPkgName}.${genClazzName}")
            val configor = configorClazz.newInstance() as Configor
            configor.config(any)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getGenPkgName(rawClazzName: String): Pair<String, String> {
        val lastIndexOfDot = rawClazzName.lastIndexOf('.')
        //包名
        val pkgName = rawClazzName.subSequence(0, lastIndexOfDot)

        //类名
        val simpleName = rawClazzName.subSequence(lastIndexOfDot + 1, rawClazzName.length)

        return Pair("${pkgName}.configor", "${simpleName}_ViewConfigor")
    }
}