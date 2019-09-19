package com.jsongo.annotation.anno

import kotlin.reflect.KClass

/**
 * @author ： jsongo
 * @date ： 19-9-9 上午11:18
 * @desc :
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Model(val clazz: KClass<out Any /*BaseModel*/>)