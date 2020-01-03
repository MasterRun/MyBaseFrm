package com.jsongo.processor

import javax.annotation.processing.*
import javax.lang.model.element.TypeElement

/**
 * @author ： jsongo
 * @date ： 2020/1/3 21:48
 * @desc : 注解处理器基类
 */
abstract class BaseProcessor : AbstractProcessor() {

    protected var fileCreated = false

    protected var mFiler: Filer? = null
    protected var mMessager: Messager? = null

    @Synchronized
    override fun init(processingEnv: ProcessingEnvironment?) {
        super.init(processingEnv)
        mMessager = processingEnv?.messager
        mFiler = processingEnv?.filer
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        //如果已经处理过注解，不再处理
        if (fileCreated) {
            return true
        }
        fileCreated = true
        return doProcess(annotations, roundEnv)
    }

    /**
     * 处理注解
     */
    abstract fun doProcess(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean
}