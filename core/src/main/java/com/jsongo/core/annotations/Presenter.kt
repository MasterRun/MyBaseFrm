package com.jsongo.core.annotations

import com.jsongo.core.mvp.base.BasePresenter
import com.jsongo.core.mvp.base.IBaseMvp
import java.lang.reflect.ParameterizedType
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
annotation class Presenter(val clazz: KClass<out BasePresenter<IBaseMvp.IBaseModel, IBaseMvp.IBaseView>>)

object PresenterBinder {

    /**
     * 缓存presenter key是"[presenter全名]@[activity/fragment toString]"
     */
    val presenterMap =
        HashMap<String, IBaseMvp.IBasePresenter<IBaseMvp.IBaseModel, IBaseMvp.IBaseView>>()

    fun bind(any: Any) {
        //IBaseView ，presenter
        if (any is IBaseMvp.IBaseView) {
            val targetClass = any::class

            //找有presenter注解的字段
            targetClass.memberProperties.forEach {
                val presenterAnnotation = it.javaField?.getAnnotation(Presenter::class.java)
                if (presenterAnnotation != null) {
                    it.isAccessible = true
                    //从注解中获取主要注入的prsenter类
                    val presenterJavaClazz = presenterAnnotation.clazz.java
                    val cacheKey = presenterJavaClazz.name + "@" + any.toString()
                    var myPresenter = presenterMap[cacheKey]
                    if (myPresenter == null) {
                        //获取泛型的类型
                        val typeParameters =
                            (presenterJavaClazz.genericSuperclass as ParameterizedType).actualTypeArguments
                        try {
                            val paramClazz = typeParameters[1] as Class<Any>
                            //泛型的第二类型就是构造方法的入参类型
                            myPresenter =
                                presenterJavaClazz.getConstructor(paramClazz).newInstance(any)
                            presenterMap[cacheKey] = myPresenter
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    //如果类型正确
                    val isInstance = it.javaField?.type?.isInstance(myPresenter)
                    if (isInstance != null && isInstance) {
                        //注入实例
                        it.javaField?.set(any, myPresenter)
                    } else {
                        throw  Exception("type incompatible")
                    }
                }
            }
        }
    }
}
