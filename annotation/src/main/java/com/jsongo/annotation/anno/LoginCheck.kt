package com.jsongo.annotation.anno

/**
 * @author ： jsongo
 * @date ： 2019/12/15 17:23
 * @desc : 注解需要登录的方法
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class LoginCheck