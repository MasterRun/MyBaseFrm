package com.jsongo.annotation.anno

import kotlin.reflect.KClass

/**
 * @author ： jsongo
 * @date ： 19-9-9 上午11:09
 * @desc :
 */
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
annotation class Presenter(val clazz: KClass<out Any>/*<out BasePresenter<IBaseMvp.IBaseModel, IBaseMvp.IBaseView>>*/)