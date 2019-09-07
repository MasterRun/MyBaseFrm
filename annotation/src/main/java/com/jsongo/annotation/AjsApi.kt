package com.jsongo.annotation

/**
 * author ： jsongo
 * createtime ： 19-8-27 下午9:21
 * desc : 注解标注
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AjsApi(val prefix: String = "", val methodName: String = "")