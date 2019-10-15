package com.jsongo.core.annotations

import com.jsongo.core.mvp.base.BaseActivity
import com.jsongo.core.mvp.base.BaseFragment
import com.jsongo.core.mvp.base.IPage
import kotlin.reflect.full.findAnnotation

/**
 * author ： jsongo
 * createtime ： 2019/7/27 0:05
 * desc :
 */
@Deprecated("use com.jsongo.annotation.anno.Page instead")
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class ConfPage(val mainLayoutId: Int, val containerIndex: Int = 1)

@Deprecated("use com.jsongo.annotation.anno.Page and it's configor instead")
object PageConfigor {
    @Deprecated("use com.jsongo.annotation.anno.Page and it's configor instead")
    fun config(any: Any) {
        if (any !is IPage) {
            return
        }
        val targetClass = any::class
        val confPage = targetClass.findAnnotation<ConfPage>()
        if (confPage != null) {
            if (any is BaseActivity) {
                any.mainLayoutId = confPage.mainLayoutId
                any.containerIndex = confPage.containerIndex
            }
            if (any is BaseFragment) {
                any.mainLayoutId = confPage.mainLayoutId
                any.containerIndex = confPage.containerIndex
            }
        }
    }

}