package com.jsongo.annotation.anno.permission

/**
 * @author ： jsongo
 * @date ： 2019/11/22 17:49
 * @desc : 标注方法表示权限拒绝
 *          此注解为aop注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class PermissionDeny