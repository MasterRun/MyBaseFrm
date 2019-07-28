package com.jsongo.core.annotations

import com.jsongo.core.mvp.base.BaseModel
import com.jsongo.core.mvp.base.IBaseMvp
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaField

/**
 * author ： jsongo
 * createtime ： 2019/7/27 20:38
 * desc :
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Model(val clazz: KClass<out BaseModel>)

object ModelBinder {

    /**
     * 缓存model key是"[model全名]@[presenter toString]"
     */
    val modelMap = HashMap<String, IBaseMvp.IBaseModel>()

    fun bind(any: Any) {
        //如果绑定的presenter ，后续注入model
        if (any is IBaseMvp.IBasePresenter<IBaseMvp.IBaseModel, IBaseMvp.IBaseView>) {
            val targetClass = any::class
            //找有model注解的字段
            targetClass.memberProperties.forEach {
                val modelAnnotation = it.javaField?.getAnnotation(Model::class.java)
                if (modelAnnotation != null) {
                    it.isAccessible = true
                    //从注解中获取主要注入的model类
                    val modelJavaClazz = modelAnnotation.clazz.java
                    val cacheKey = modelJavaClazz.name + "@" + any.toString()
                    //获取指定model的实例
                    var myModel = modelMap[cacheKey]
                    if (myModel == null) {
                        myModel = modelJavaClazz.newInstance()
                        modelMap[cacheKey] = myModel
                    }
                    //如果类型正确
                    val isInstance = it.javaField?.type?.isInstance(myModel)
                    if (isInstance != null && isInstance) {
                        //注入实例
                        it.javaField?.set(any, myModel)
                    } else {
                        throw  Exception("type incompatible")
                    }
                }
            }
        }
    }
}
