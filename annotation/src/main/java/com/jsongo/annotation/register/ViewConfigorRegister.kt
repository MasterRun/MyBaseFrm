package com.jsongo.annotation.register

import com.jsongo.annotation.configor.Configor
import com.jsongo.annotation.util.Util

/**
 * @author ： jsongo
 * @date ： 19-9-19 下午11:25
 * @desc :
 */
object ViewConfigorRegister {
    fun config(any: Any) {
        val name = any.javaClass.name
        val (pkgName, clazzName) = Util.getPkgClazzName(name)
        try {
            val configorClazz =
                Class.forName("${pkgName}${pkgSuffix}.${clazzName}${clazzNameSuffix}")
            val configor = configorClazz.newInstance() as Configor
            configor.config(any)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    const val pkgSuffix = ".configor"
    const val clazzNameSuffix = "_ViewConfigor"
}