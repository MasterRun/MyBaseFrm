package com.jsongo.annotation.register

import com.jsongo.annotation.configor.Configor
import com.jsongo.annotation.util.Util

/**
 * @author ： jsongo
 * @date ： 19-9-20 下午8:26
 * @desc :用于配置mvp的Presenter   model注入
 */
object PresenterConfigor {
    fun config(any: Any) {
        val name = any.javaClass.name
        val (pkgName, clazzName) = Util.getPkgClazzName(name)
        try {
            val configorClazz =
                Class.forName("${pkgName}${pkgSuffix}.${clazzName}${clazzNameSuffix}")
            val configor = configorClazz.newInstance() as Configor
            configor.config(any)
        } catch (e: Exception) {
            if (e is ClassNotFoundException) {
                System.err.println("Exception on PresenterConfigor: ClassNotFoundException for  " + e.message)
            } else {
                e.printStackTrace()
            }
        }
    }

    const val pkgSuffix = ".configor"
    const val clazzNameSuffix = "_PresenterConfigor"
}