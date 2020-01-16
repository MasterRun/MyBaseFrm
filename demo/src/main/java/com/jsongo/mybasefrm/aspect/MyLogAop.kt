package com.jsongo.mybasefrm.aspect

import android.util.Log
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
//标识类为切面
//@Aspect
class MyLogAop {

    val methodCount = HashMap<String, HashMap<String, Int>>()

    /**
     * 定义切点
     */
    //@Pointcut("execution(* com.jsongo.mybasefrm.view..*.onC*(..))")  //  --  ok
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter.DemoPresenter.*(..))")
    //@Pointcut("execution(* com.jsongo.mybasefrm.view.activity.DemoActivity.onGetDailyGank(..))")//  --ok
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter.DemoPresenter.getAuthtypes(..))")
    //@Pointcut("execution(* com.jsongo.mybasefrm.presenter..*(..))")
    //@Pointcut("execution(android.view.View.OnClickListener.onClick(..))")
//    @Pointcut("execution(* com.jsongo.core.mvp.base.BaseActivity.onCreate(..))")  // -- ok
    /*@Pointcut("execution(* com.jsongo.core..*.**(..))")  // --  ok
    fun methodLog() {
    }*/


    /**
     * 定义一个切面方法，包裹切点
     */
    /*@Around("methodLog()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint): Any? {
        //获取方法
        val methodSignature = joinPoint.signature as MethodSignature
        //方法所在类名
        val className = methodSignature.declaringType.name
        //方法名
        val methodName = methodSignature.name
        //方法参数名
        val parameterNames = methodSignature.parameterNames
        //方法参数类型
        val parameterTypes = methodSignature.parameterTypes

        //切点（方法）参数
        val args = joinPoint.args
        //切点类型（方法）
        val kind = joinPoint.kind
        //切点（方法）源码位置
        val sourceLocation = joinPoint.sourceLocation

        //打印日志
        Log.e("MyLogAop", "before --  ${className}#${methodName}")
        //执行方法
        val proceed = joinPoint.proceed()

        doCount(className, methodName)

        //打印日志
        Log.e("MyLogAop", "after --  ${className}#${methodName}")

        //返回执行结果
        return proceed
    }*/

    /**
     * 记录方法执行次数
     */
    fun doCount(className: String, methodName: String) {
        var hashMap = methodCount[className]
        if (hashMap == null) {
            hashMap = HashMap()
        }
        val i = hashMap[methodName]
        hashMap[methodName] = (i ?: 0) + 1
        methodCount[className] = hashMap
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