package com.jsongo.ajs.annotations

import com.jsongo.ajs.interaction.register.BaseInteractionRegister

/**
 * author ： jsongo
 * createtime ： 19-8-27 下午9:21
 * desc : 注解标注
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AjsApi

/**
 * 交互api列表
 */
val interactionRegisterList = emptyList<BaseInteractionRegister>()

object AjsApiBinder {

    fun bind(any: Any) {
        if (any !is BaseInteractionRegister) {
            return
        }
    }
}