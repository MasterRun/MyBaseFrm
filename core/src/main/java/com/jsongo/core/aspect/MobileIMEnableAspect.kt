package com.jsongo.core.aspect

import com.jsongo.core.BaseCore
import com.jsongo.core.R
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * @author ： jsongo
 * @date ： 2019/11/22 23:43
 * @desc : 过滤mobileim功能切面
 */
@Aspect
class MobileIMEnableAspect {

    //此处参数是注解对象
    @Pointcut("execution(@com.jsongo.annotation.anno.WhenMobileIMEnable * *(..))")
    fun mobileIMEnablePointCut() {
    }

    @Around("mobileIMEnablePointCut()")
    @Throws(Throwable::class)
    fun mobileIMEnableExecutor(joinPoint: ProceedingJoinPoint): Any? {
        if (BaseCore.context.resources.getBoolean(R.bool.enable_mobile_im)) {
            return joinPoint.proceed()
        }
        return null

    }
}