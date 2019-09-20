package com.jsongo.annotation.register

import com.jsongo.annotation.configor.Configor
import com.jsongo.annotation.util.Util

/**
 * @author ： jsongo
 * @date ： 19-9-19 下午11:25
 * @desc :用于配置mvp的View  ，配置布局id，容器id，以及presenter的注入
 */
object ViewConfigor {
    fun config(any: Any) {
        val name = any.javaClass.name
        val (pkgName, clazzName) = Util.getPkgClazzName(name)
        try {
            val configorClazz =
                Class.forName("${pkgName}${pkgSuffix}.${clazzName}${clazzNameSuffix}")
            val configor = configorClazz.newInstance() as Configor
            configor.config(any)
        } catch (e: Exception) {
//            e.printStackTrace()
            if (e is ClassNotFoundException) {
                System.err.println("Exception on ViewConfigor: ClassNotFoundException for  " + e.message)
            }
        }
    }

    const val pkgSuffix = ".configor"
    const val clazzNameSuffix = "_ViewConfigor"
}