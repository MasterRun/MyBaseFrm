package com.jsongo.annotation.anno

/**
 * author ： jsongo
 * createtime ： 19-8-27 下午9:21
 * desc : 声明方法为 ajs api
 *          此注解为apt注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class AjsApi(val prefix: String = "", val methodName: String = "")