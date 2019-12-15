package com.jsongo.mybasefrm.aspect

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * @author ： jsongo
 * @date ： 19-10-8 下午10:58
 * @desc :
 */
/*
@Aspect
class DbAop {

    @Pointcut("execution(* com.jsongo.core.db.CommonDbOpenHelper.Companion.getValue(..))")
    fun dbAop() {
    }

    @Around("dbAop()")
    @Throws(Throwable::class)
    fun aroundPointcut(joinPoint: JoinPoint): Any? {
        return "value from aop"
    }

    @Around("execution(* com.jsongo.ajs.interaction.Topbar.title(..))")
    fun aroundB(joinPoint: ProceedingJoinPoint): Any? {
//        val args = joinPoint.args
        return joinPoint.proceed()
    }
}*/
