package com.jsongo.ajs.helper

import com.jsongo.ajs.interaction.register.BaseInteractionRegister
import com.jsongo.ajs.interaction.register.DefaultInteractionRegister
import com.safframework.log.L

/**
 * author ： jsongo
 * createtime ： 19-8-29 下午11:16
 * desc :
 */
object InteractionRegisterCollector {
    val interactionRegisterList = ArrayList<BaseInteractionRegister>()

    init {
        interactionRegisterList.add(DefaultInteractionRegister)

        try {
            val customInteractionRegisterClazz =
                Class.forName("com.jsongo.ajs.helper.CustomInteractionRegister_Gen")
            val customInteractionRegister =
                customInteractionRegisterClazz.getConstructor().newInstance() as BaseInteractionRegister
            interactionRegisterList.add(customInteractionRegister)
        } catch (e: Exception) {
            if (e is ClassNotFoundException) {
                L.d("ClassNotFoundException :" + e.message)
            } else {
                e.printStackTrace()
            }
        }

    }
}