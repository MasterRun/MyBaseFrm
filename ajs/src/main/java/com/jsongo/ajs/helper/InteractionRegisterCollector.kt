package com.jsongo.ajs.helper

import com.jsongo.ajs.interaction.register.BaseInteractionRegister
import com.jsongo.ajs.interaction.register.DefaultInteractionRegister

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
            val collectorGenClazz =
                Class.forName("com.jsongo.ajs.helper.InteractionRegisterCollector_Gen")
            val list =
                collectorGenClazz.getDeclaredMethod("getInteractionRegisterList").invoke(null) as List<BaseInteractionRegister>
            interactionRegisterList.addAll(list)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}