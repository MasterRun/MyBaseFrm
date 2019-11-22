package com.jsongo.annotation.anno

/**
 * @author ： jsongo
 * @date ： 19-9-9 上午11:08
 * @desc : 用于配置页面
 *          此注解为apt注解
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Page(val mainLayoutId: Int, val containerIndex: Int = 1)