package com.jsongo.mybasefrm.aspect

/**
 * @author ： jsongo
 * @date ： 19-9-22 下午11:10
 * @desc :
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class AopOnclick(
    /**
     * 点击间隔时间
     */
    val value: Long = 1000
)