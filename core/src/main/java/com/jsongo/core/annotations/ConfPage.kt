package com.jsongo.core.annotations

import com.jsongo.core.mvp.base.IPage
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

/**
 * author ： jsongo
 * createtime ： 2019/7/27 0:05
 * desc :
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfPage(val mainLayoutId: Int, val containerIndex: Int = 1)

object ConfPageProcessor {
    fun config(any: Any) {
        if (any !is IPage) {
            return
        }
        val targetClass = any::class
        val confPage = targetClass.findAnnotation<ConfPage>()
        if (confPage != null) {
            targetClass.declaredMemberProperties
            targetClass.memberProperties.forEach {
                it.isAccessible = true
                if (it.name.equals("mainLayoutId")) {
                    it.javaField?.apply {
                        set(any, confPage.mainLayoutId)
                    }
                } else if (it.name.equals("containerIndex")) {
                    it.javaField?.apply {
                        set(any, confPage.containerIndex)
                    }
                }
            }
        }
    }

}