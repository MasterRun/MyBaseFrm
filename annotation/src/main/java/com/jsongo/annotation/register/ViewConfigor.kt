package com.jsongo.annotation.register

import com.jsongo.annotation.configor.Configor
import com.jsongo.annotation.util.Util

/**
 * @author ： jsongo
 * @date ： 19-9-19 下午11:25
 * @desc : 入口类 调用config方法使用生成的 configor 配置View  ，配置布局id，容器id
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
            if (e is ClassNotFoundException) {
                System.err.println("Exception on ViewConfigor: ClassNotFoundException for  " + e.message)
            } else {
                e.printStackTrace()
            }
        }
    }

    const val pkgSuffix = ".configor"
    const val clazzNameSuffix = "_ViewConfigor"
}