package com.jsongo.annotation.register

import com.jsongo.annotation.configor.Configor
import com.jsongo.annotation.util.Util

/**
 * @author ： jsongo
 * @date ： 19-9-19 下午11:25
 * @desc : 入口类 调用config方法使用生成的 configor 配置View  ，配置布局id，容器id
 */
object ViewConfigor {

    /**
     * 缓存configor
     */
    val configorMap = HashMap<String, Configor>()

    fun config(any: Any) {
        val name = any.javaClass.name
        val (pkgName, clazzName) = Util.getPkgClazzName(name)
        val configorClazzName = "${pkgName}${pkgSuffix}.${clazzName}${clazzNameSuffix}"
        var configor = configorMap[configorClazzName]
        if (configor == null) {
            try {
                val configorClazz =
                    Class.forName(configorClazzName)
                configor = configorClazz.newInstance() as Configor
                configorMap[configorClazzName] = configor
            } catch (e: Exception) {
                if (e is ClassNotFoundException) {
                    System.err.println("Exception on ViewConfigor: ClassNotFoundException for  " + e.message)
                } else {
                    e.printStackTrace()
                }
            }
        }
        configor?.config(any)
    }

    const val pkgSuffix = ".configor"
    const val clazzNameSuffix = "_ViewConfigor"
}