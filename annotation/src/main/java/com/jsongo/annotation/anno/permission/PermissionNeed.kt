package com.jsongo.annotation.anno.permission

/**
 * @author ： jsongo
 * @date ： 2019/11/20 19:40
 * @desc : 标注需要权限，只能使用在无返回值的方法上
 *          此注解为aop注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionNeed(vararg val permissions: String)