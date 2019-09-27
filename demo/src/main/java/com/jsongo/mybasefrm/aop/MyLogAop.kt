package com.jsongo.mybasefrm.aop

import com.safframework.log.L
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

/**
 * @author ： jsongo
 * @date ： 19-9-22 下午11:26
 * @desc :
 */
@Aspect
class MyLogAop {

    /**
     * 定义切点，标记切点为所有被@AopOnclick注解的方法
     * 注意：这里@com.freak.kotlinhttpmanager.aop.AopOnclick需要替换成
     * 你自己项目中AopOnclick这个类的全路径
     */
    //@Pointcut("execution(* com.jsongo.mybasefrm.view..*.onC*(..))")  //  --  ok
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter.MainPresenter.*(..))")
    //@Pointcut("execution(* com.jsongo.mybasefrm.view.activity.MainActivity.onGetDailyGank(..))")//  --ok
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter.MainPresenter.getAuthtypes(..))")
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter..*(..))")
    //@Pointcut("execution(android.view.View.OnClickListener.onClick(..))")
//    @Pointcut("execution(* com.jsongo.core.mvp.base.BaseActivity.onCreate(..))")  // -- ok
    @Pointcut("execution(* android.app.Activity.on**(..))")  // --  ok
    fun methodLog() {
    }


    /**
     * 定义一个切面方法，包裹切点方法
     */
    @Around("methodLog()")
//@Around("execution(* android.app.Activity.on**(..))")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint): Any? {
        val methodSignature = joinPoint.signature as MethodSignature
        val className = methodSignature.declaringType.simpleName
        val methodName = methodSignature.name
        L.w("before --  ${className}#${methodName}")
        val proceed = joinPoint.proceed()
        L.w("after --  ${className}#${methodName}")
        return proceed
    }

    /* @Before("execution(* android.app.Activity.on**(..))")
     fun beforeOnMethod(joinPoint: JoinPoint) {
         val methodSignature = joinPoint.signature as MethodSignature
         val className = methodSignature.declaringType.simpleName
         val methodName = methodSignature.name
         L.w("before --  ${className}#${methodName}")
     }

     @After("execution(* android.app.Activity.on**(..))")
     fun afterOnMethod(joinPoint: JoinPoint) {
         val methodSignature = joinPoint.signature as MethodSignature
         val className = methodSignature.declaringType.simpleName
         val methodName = methodSignature.name
         L.w("after --  ${className}#${methodName}")
     }*/
}